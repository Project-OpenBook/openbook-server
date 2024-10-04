package com.openbook.openbook.domain.booth;

import com.openbook.openbook.domain.EntityBasicTime;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.booth.dto.BoothReviewUpdateData;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothReview extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @Max(5)
    private float star;

    private String content;

    private String imageUrl;

    @Builder
    public BoothReview(User reviewer, Booth linkedBooth, float star, String content, String imageUrl){
        this.reviewer = reviewer;
        this.linkedBooth = linkedBooth;
        this.star = star;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void updateReview(BoothReviewUpdateData updateData){
        if(updateData.star() != null){
            this.star = updateData.star();
        }
        if(updateData.content() != null){
            this.content = updateData.content();
        }
        if(updateData.image() != null){
            this.imageUrl = updateData.image();
        }
    }
}
