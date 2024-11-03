package com.openbook.openbook.service.booth.dto;

import java.util.List;

public record BoothReservationDateDto(
        String date,
        List<BoothReservationDetailDto> times
) {
}
