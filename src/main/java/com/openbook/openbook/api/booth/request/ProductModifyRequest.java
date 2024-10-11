package com.openbook.openbook.api.booth.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ProductModifyRequest(
        String name,
        String description,
        Integer stock,
        Integer price,
        Long categoryId,
        List<MultipartFile> imageToAdd,
        List<Long> imageToDelete
) {
}
