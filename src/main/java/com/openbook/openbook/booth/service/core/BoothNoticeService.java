package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.dto.BoothNoticeDto;
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

@Service
@RequiredArgsConstructor
public class BoothNoticeService {
    private final BoothNoticeRepository boothNoticeRepository;
    private final S3Service s3Service;

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

    public BoothNotice getBoothNoticeOrException(Long id){
        return boothNoticeRepository.findById(id).orElseThrow(
                () -> new OpenBookException(ErrorCode.BOOTH_NOTICE_NOT_FOUND)
        );
    }

}
