package com.openbook.openbook.booth.dto;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothNoticeType;

public record BoothNoticeDto(
        String title,
        String content,
        String imageUrl,
        BoothNoticeType type,
        Booth linkedBooth
) {
}
