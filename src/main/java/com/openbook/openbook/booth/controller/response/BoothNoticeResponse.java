package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothNotice;
import com.openbook.openbook.booth.entity.dto.BoothNoticeType;

import java.time.LocalDateTime;

public record BoothNoticeResponse(
        long id,
        String title,
        String content,
        String imageUrl,
        BoothNoticeType type,
        LocalDateTime registeredAt,
        long boothId,
        String boothName,
        long boothManagerId
) {
    public static BoothNoticeResponse of(BoothNotice boothNotice){
        return new BoothNoticeResponse(
                boothNotice.getId(),
                boothNotice.getTitle(),
                boothNotice.getContent(),
                boothNotice.getImageUrl(),
                BoothNoticeType.valueOf(boothNotice.getType()),
                boothNotice.getRegisteredAt(),
                boothNotice.getLinkedBooth().getId(),
                boothNotice.getLinkedBooth().getName(),
                boothNotice.getLinkedBooth().getManager().getId()
        );
    }
}
