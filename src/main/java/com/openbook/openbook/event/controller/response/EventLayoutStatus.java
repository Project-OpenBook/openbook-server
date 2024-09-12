package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.booth.service.dto.BoothAreaStatusData;
import com.openbook.openbook.event.entity.dto.EventLayoutType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;
import java.util.Map;

public record EventLayoutStatus(
        List<String> layoutImageUrls,
        @Enumerated(EnumType.STRING)
        EventLayoutType layoutType,
        Map<String, List<BoothAreaStatusData>> areas
) {
}
