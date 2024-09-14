package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

public record BoothReviewDto(
        User reviewer,
        Booth linkedBooth,
        float star,
        String content,
        MultipartFile image
) {
}
