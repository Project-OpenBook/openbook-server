package com.openbook.openbook.eventmanager.dto;

import com.openbook.openbook.event.entity.EventLayoutArea;

public record BoothLocationData(
        String classification,
        String number
) {
    public static BoothLocationData of(EventLayoutArea eventLayoutArea){
        return new BoothLocationData(
                eventLayoutArea.getClassification(),
                eventLayoutArea.getNumber()
        );
    }
}
