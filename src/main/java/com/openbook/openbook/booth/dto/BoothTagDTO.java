package com.openbook.openbook.booth.dto;

import com.openbook.openbook.booth.entity.Booth;
import lombok.Builder;

@Builder
public record BoothTagDTO(
        String content,
        Booth booth
) {
}
