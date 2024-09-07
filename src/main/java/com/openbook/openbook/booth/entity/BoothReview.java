package com.openbook.openbook.booth.entity;

import com.openbook.openbook.global.util.EntityBasicTime;
import com.openbook.openbook.user.entity.User;
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
}
