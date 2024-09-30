package com.openbook.openbook.service.event.dto;

import com.openbook.openbook.domain.event.dto.EventNoticeType;
import lombok.Builder;

@Builder
public record EventNoticeUpdateData(
        String title,
        String content,
        EventNoticeType type,
        String imageUrl
) {

}

