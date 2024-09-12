package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.service.dto.BoothNoticeDto;
import java.time.LocalDateTime;

public record BoothNoticeResponse(
        long id,
        String title,
        String content,
        String imageUrl,
        String type,
        LocalDateTime registeredAt,
        BoothPublicResponse booth
) {
    public static BoothNoticeResponse of(BoothNoticeDto boothNotice){
        return new BoothNoticeResponse(
                boothNotice.id(),
                boothNotice.title(),
                boothNotice.content(),
                boothNotice.imageUrl(),
                boothNotice.type(),
                boothNotice.registeredAt(),
                BoothPublicResponse.of(boothNotice.linkedBooth())
        );
    }
}
