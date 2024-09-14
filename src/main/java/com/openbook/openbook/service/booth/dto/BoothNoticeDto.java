package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.BoothNotice;
import java.time.LocalDateTime;

public record BoothNoticeDto(
        long id,
        String title,
        String content,
        String imageUrl,
        String type,
        LocalDateTime registeredAt,
        BoothDto linkedBooth
) {
    public static BoothNoticeDto of(BoothNotice notice, BoothDto booth) {
        return new BoothNoticeDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getImageUrl(),
                notice.getType(),
                notice.getRegisteredAt(),
                booth
        );
    }
}
