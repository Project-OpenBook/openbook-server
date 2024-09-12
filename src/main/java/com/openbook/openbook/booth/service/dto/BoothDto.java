package com.openbook.openbook.booth.service.dto;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.event.dto.EventDto;
import com.openbook.openbook.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.List;

public record BoothDto(
        long id,
        String name,
        String description,
        String mainImageUrl,
        String accountNumber,
        String accountBankName,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        List<BoothAreaDto> locations,
        List<BoothTagDto> tags,
        String status,
        LocalDateTime registeredAt,
        UserDto manager,
        EventDto linkedEvent
) {
    public static BoothDto of(Booth booth, List<BoothAreaDto> locations) {
        return new BoothDto(
                booth.getId(),
                booth.getName(),
                booth.getDescription(),
                booth.getMainImageUrl(),
                booth.getAccountNumber(),
                booth.getAccountBankName(),
                booth.getOpenTime(),
                booth.getCloseTime(),
                locations,
                booth.getBoothTags().stream().map(BoothTagDto::of).toList(),
                booth.getStatus().name(),
                booth.getRegisteredAt(),
                UserDto.of(booth.getManager()),
                EventDto.of(booth.getLinkedEvent())
        );
    }
}
