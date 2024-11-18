package com.openbook.openbook.api.booth.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record ReserveModifyRequest(
        String name,
        String description,
        MultipartFile image,
        Integer price,
        LocalDate date,
        List<String> timesToAdd,
        List<Long> timesToDelete
) {
}
