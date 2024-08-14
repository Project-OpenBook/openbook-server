package com.openbook.openbook.booth.dto;

import com.openbook.openbook.booth.entity.Booth;

public record BoothProductDto(
        String name,
        String description,
        int stock,
        int price,
        Booth linkedBooth
) {
}
