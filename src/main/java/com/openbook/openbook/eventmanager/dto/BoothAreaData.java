package com.openbook.openbook.eventmanager.dto;

import com.openbook.openbook.event.entity.EventLayoutArea;

public record BoothAreaData(
        String classification,
        String number
) {
    public static BoothAreaData of(EventLayoutArea eventLayoutArea){
        return new BoothAreaData(
                eventLayoutArea.getClassification(),
                eventLayoutArea.getNumber()
        );
    }
}
