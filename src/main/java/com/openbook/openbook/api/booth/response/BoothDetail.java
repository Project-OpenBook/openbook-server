package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.service.booth.dto.BoothAreaDto;
import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.service.booth.dto.BoothTagDto;
import com.openbook.openbook.api.event.response.EventPublicResponse;
import com.openbook.openbook.api.user.response.UserPublicResponse;

import java.time.LocalDate;
import java.util.List;

import static com.openbook.openbook.util.Formatter.getFormattingTime;

public record BoothDetail(
        Long id,
        String name,
        String description,
        String mainImageUrl,
        String openTime,
        String closeTime,
        LocalDate openDate,
        LocalDate closeDate,
        List<BoothAreaDto> location,
        List<String> tags,
        UserPublicResponse manager,
        EventPublicResponse event
) {
    public static BoothDetail of(BoothDto booth){
        return new BoothDetail(
                booth.id(),
                booth.name(),
                booth.description(),
                booth.mainImageUrl(),
                getFormattingTime(booth.openTime()),
                getFormattingTime(booth.closeTime()),
                booth.linkedEvent().openDate(),
                booth.linkedEvent().closeDate(),
                booth.locations(),
                booth.tags().stream().map(BoothTagDto::name).toList(),
                UserPublicResponse.of(booth.manager()),
                EventPublicResponse.of(booth.linkedEvent())
        );
    }
}
