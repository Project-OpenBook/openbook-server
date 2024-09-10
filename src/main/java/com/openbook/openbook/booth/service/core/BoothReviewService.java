package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.dto.BoothReviewDto;
import com.openbook.openbook.booth.entity.BoothReview;
import com.openbook.openbook.booth.repository.BoothReviewRepository;
import com.openbook.openbook.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothReviewService {
    private final BoothReviewRepository boothReviewRepository;
    private final S3Service s3Service;

    public void createBoothReview(BoothReviewDto reviewDto){
        boothReviewRepository.save(
            BoothReview.builder()
                    .reviewer(reviewDto.reviewer())
                    .linkedBooth(reviewDto.linkedBooth())
                    .star(reviewDto.star())
                    .content(reviewDto.content())
                    .imageUrl(s3Service.uploadFileAndGetUrl(reviewDto.image()))
                    .build()
        );
    }
}
