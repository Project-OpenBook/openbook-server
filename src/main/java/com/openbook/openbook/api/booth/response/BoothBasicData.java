package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.service.booth.dto.BoothTagDto;
import java.util.List;

import static com.openbook.openbook.util.Formatter.getFormattingDate;

public record BoothBasicData(
        Long id,
        String name,
        String eventName,
        String openDate,
        String closeDate,
        String mainImageUrl,
        List<String> tags
) {
    public static BoothBasicData of(BoothDto booth){
        return new BoothBasicData(
                booth.id(),
                booth.name(),
                booth.linkedEvent().name(),
                getFormattingDate(booth.linkedEvent().openDate().atStartOfDay()),
                getFormattingDate(booth.linkedEvent().closeDate().atStartOfDay()),
                booth.mainImageUrl(),
                booth.tags().stream().map(BoothTagDto::name).toList()
        );
    }
}
