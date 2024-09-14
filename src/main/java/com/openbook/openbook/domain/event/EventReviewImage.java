package com.openbook.openbook.domain.event;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventReview linkedReview;

    private String imageUrl;

    @Max(4)
    private int imageOrder;

    @Builder
    public EventReviewImage(EventReview linkedReview, String imageUrl, int imageOrder) {
        this.linkedReview = linkedReview;
        this.imageUrl = imageUrl;
        this.imageOrder = imageOrder;
    }

}
