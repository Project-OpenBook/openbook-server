package com.openbook.openbook.booth.dto;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public record BoothReviewDto(
        User reviewer,
        Booth linkedBooth,
        float star,
        String content,
        MultipartFile image
) {
}
