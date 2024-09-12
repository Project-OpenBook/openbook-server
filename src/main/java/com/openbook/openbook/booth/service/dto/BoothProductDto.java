package com.openbook.openbook.booth.service.dto;

import com.openbook.openbook.booth.entity.BoothProductCategory;
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
