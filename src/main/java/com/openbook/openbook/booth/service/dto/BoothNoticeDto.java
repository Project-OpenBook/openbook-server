package com.openbook.openbook.booth.service.dto;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothNoticeType;
import org.springframework.web.multipart.MultipartFile;

public record BoothNoticeDto(
        String title,
        String content,
        MultipartFile imageUrl,
        BoothNoticeType type,
        Booth linkedBooth
) {
}
