package com.openbook.openbook.booth.dto;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.user.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BoothDTO(
        User manager,
        Event linkedEvent,
        String name,
        String description,
        String mainImageUrl,
        String accountNumber,
        String accountBankName,
        LocalDateTime openTime,
        LocalDateTime closeTime
) {
}
