package com.openbook.openbook.event.dto;


import com.openbook.openbook.event.entity.EventLayout;
import lombok.Builder;

@Builder
public record LayoutAreaDTO(
        EventLayout linkedEventLayout,
        EventLayoutAreaStatus status,
        String classification,
        String number
) {
}
