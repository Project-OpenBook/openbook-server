package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothArea;

public record BoothAreaData(
        String classification,
        String number
) {
    public static BoothAreaData of(BoothArea boothArea){
        return new BoothAreaData(
                boothArea.getClassification(),
                boothArea.getNumber()
        );
    }
}
