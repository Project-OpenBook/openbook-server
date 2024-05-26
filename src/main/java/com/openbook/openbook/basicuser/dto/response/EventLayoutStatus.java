package com.openbook.openbook.basicuser.dto.response;

import com.openbook.openbook.basicuser.dto.LayoutAreaStatusData;
import com.openbook.openbook.event.dto.EventLayoutType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;
import java.util.Map;

public record EventLayoutStatus(
        List<String> layoutImageUrls,
        @Enumerated(EnumType.STRING)
        EventLayoutType layoutType,
        Map<String, List<LayoutAreaStatusData>> areas
) {
}
