package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.dto.BoothNoticeDto;
import com.openbook.openbook.booth.entity.BoothNotice;
import com.openbook.openbook.booth.repository.BoothNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothNoticeService {
    private final BoothNoticeRepository boothNoticeRepository;

    public void createBoothNotice(BoothNoticeDto boothNoticeDto){
        boothNoticeRepository.save(
                BoothNotice.builder()
                        .title(boothNoticeDto.title())
                        .content(boothNoticeDto.content())
                        .type(boothNoticeDto.type())
                        .imageUrl(boothNoticeDto.imageUrl())
                        .linkedBooth(boothNoticeDto.linkedBooth())
                        .build()
        );
    }
}
