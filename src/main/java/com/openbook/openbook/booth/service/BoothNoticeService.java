package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.controller.request.BoothNoticeRegisterRequest;
import com.openbook.openbook.booth.controller.response.BoothNoticeResponse;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.dto.BoothNoticeDto;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothNotice;
import com.openbook.openbook.booth.repository.BoothNoticeRepository;
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
    private final BoothNoticeRepository boothNoticeRepository;
    private final S3Service s3Service;
    private final BoothService boothService;

    public BoothNotice getBoothNoticeOrException(Long id){
        return boothNoticeRepository.findById(id).orElseThrow(
                () -> new OpenBookException(ErrorCode.BOOTH_NOTICE_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public Slice<BoothNoticeResponse> getBoothNotices(Long boothId, Pageable pageable){
        Booth booth = boothService.getBoothOrException(boothId);
        return getNotices(booth, pageable).map(BoothNoticeResponse::of);
    }

    @Transactional(readOnly = true)
    public BoothNoticeResponse getBoothNotice(Long boothId){
        return BoothNoticeResponse.of(getBoothNoticeOrException(boothId));
    }

    @Transactional
    public void registerBoothNotice(Long userId, Long boothId, BoothNoticeRegisterRequest request){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getManager().getId().equals(userId) || !booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        createBoothNotice(new BoothNoticeDto(
                request.title(), request.content(), request.image(), request.noticeType(), booth
        ));
    }

    public void createBoothNotice(BoothNoticeDto boothNoticeDto){
        boothNoticeRepository.save(
                BoothNotice.builder()
                        .title(boothNoticeDto.title())
                        .content(boothNoticeDto.content())
                        .type(boothNoticeDto.type())
                        .imageUrl(s3Service.uploadFileAndGetUrl(boothNoticeDto.imageUrl()))
                        .linkedBooth(boothNoticeDto.linkedBooth())
                        .build()
        );
    }

    public Slice<BoothNotice> getNotices(Booth booth, Pageable pageable){
        return boothNoticeRepository.findByLinkedBoothId(booth.getId(), pageable);
    }



}
