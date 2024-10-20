package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.service.booth.dto.BoothAreaDto;
import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.service.booth.dto.BoothTagDto;
import com.openbook.openbook.api.event.response.EventPublicResponse;
import com.openbook.openbook.api.user.response.UserPublicResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.openbook.openbook.util.Formatter.getFormattingTime;

public record BoothDetail(
        Long id,
        String name,
        String description,
        String mainImageUrl,
        LocalDateTime openData,
        LocalDateTime closeData,
        String accountNumber,
        String accountBankName,
        List<BoothAreaDto> location,
        List<BoothTagDto> tags,
        UserPublicResponse manager,
        EventPublicResponse event
) {
    public static BoothDetail of(BoothDto booth){
        return new BoothDetail(
                booth.id(),
                booth.name(),
                booth.description(),
                booth.mainImageUrl(),
                booth.openTime(),
                booth.closeTime(),
                booth.accountNumber(),
                booth.accountBankName(),
                booth.locations(),
                booth.tags(),
                UserPublicResponse.of(booth.manager()),
                EventPublicResponse.of(booth.linkedEvent())
        );
    }
}
