package com.openbook.openbook.service.booth;

import com.openbook.openbook.api.booth.request.BoothNoticeModifyRequest;
import com.openbook.openbook.api.booth.request.BoothNoticeRegisterRequest;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothNotice;
import com.openbook.openbook.repository.booth.BoothNoticeRepository;
import com.openbook.openbook.service.booth.dto.BoothNoticeDto;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.booth.dto.BoothNoticeUpdateData;
import com.openbook.openbook.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothNoticeService {

    private final S3Service s3Service;
    private final BoothService boothService;
    private final BoothNoticeRepository boothNoticeRepository;

    public BoothNotice getBoothNoticeOrException(Long id){
        return boothNoticeRepository.findById(id).orElseThrow(
                () -> new OpenBookException(ErrorCode.BOOTH_NOTICE_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public Slice<BoothNoticeDto> getBoothNotices(Long boothId, Pageable pageable){
        boothService.getBoothOrException(boothId);
        return boothNoticeRepository.findByLinkedBoothId(boothId, pageable).map(
                notice -> BoothNoticeDto.of(notice, boothService.getBoothById(boothId))
        );
    }

    @Transactional(readOnly = true)
    public BoothNoticeDto getBoothNotice(Long noticeId){
        BoothNotice notice = getBoothNoticeOrException(noticeId);
        return BoothNoticeDto.of(notice, boothService.getBoothById(notice.getLinkedBooth().getId()));
    }

    @Transactional
    public void registerBoothNotice(Long userId, Long boothId, BoothNoticeRegisterRequest request){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getManager().getId().equals(userId) || !booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        boothNoticeRepository.save(
                BoothNotice.builder()
                        .title(request.title())
                        .content(request.content())
                        .type(request.noticeType())
                        .imageUrl((request.image() != null)?s3Service.uploadFileAndGetUrl(request.image()):null)
                        .linkedBooth(booth)
                        .build()
        );
    }

    @Transactional
    public void modifyNotice(long userId, long noticeId, BoothNoticeModifyRequest request){
        BoothNotice notice = getBoothNoticeOrException(noticeId);
        if(!notice.getLinkedBooth().getManager().getId().equals(userId)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }

        notice.updateNotice(BoothNoticeUpdateData.builder()
                        .title(request.title())
                        .content(request.content())
                        .type(request.noticeType())
                        .imageUrl((request.image() != null)?s3Service.uploadFileAndGetUrl(request.image()):null)
                        .build());
    }

    @Transactional
    public void deleteBoothNotice(Long userId, Long noticeId){
        BoothNotice notice = getBoothNoticeOrException(noticeId);
        VerifyUserIsManagerOfBooth(notice, userId);
        s3Service.deleteFileFromS3(notice.getImageUrl());
        boothNoticeRepository.delete(notice);
    }

    private void VerifyUserIsManagerOfBooth(BoothNotice boothNotice, long userId){
        if(!boothNotice.getLinkedBooth().getManager().getId().equals(userId)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

}
