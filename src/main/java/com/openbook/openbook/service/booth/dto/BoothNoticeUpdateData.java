package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.dto.BoothNoticeType;
import lombok.Builder;

@Builder
public record BoothNoticeUpdateData(
        String title,
        String content,
        BoothNoticeType type,
        String imageUrl
) {
}
