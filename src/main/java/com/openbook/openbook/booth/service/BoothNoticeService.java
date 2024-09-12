package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.controller.request.BoothNoticeRegisterRequest;
import com.openbook.openbook.booth.controller.response.BoothNoticeResponse;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothNotice;
import com.openbook.openbook.booth.repository.BoothNoticeRepository;
import com.openbook.openbook.booth.service.dto.BoothNoticeDto;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
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
    public BoothNoticeDto getBoothNotice(Long boothId){
        return BoothNoticeDto.of(getBoothNoticeOrException(boothId), boothService.getBoothById(boothId));
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
                        .imageUrl(s3Service.uploadFileAndGetUrl(request.image()))
                        .linkedBooth(booth)
                        .build()
        );
    }

}
