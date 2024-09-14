package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.BoothProductCategory;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record BoothProductDto(
        String name,
        String description,
        int stock,
        int price,
        List<MultipartFile> images,
        BoothProductCategory linkedCategory
) {
}
