package com.openbook.openbook.domain.event;


import com.openbook.openbook.domain.EntityBasicTime;
import com.openbook.openbook.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventReview extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event linkedEvent;

    @Max(5)
    private float star;

    private String content;

    @OneToMany(mappedBy = "linkedReview", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public EventReview(User reviewer, Event linkedEvent, float star, String content) {
        this.reviewer = reviewer;
        this.linkedEvent = linkedEvent;
        this.star = star;
        this.content = content;
    }

    public void update(float star, String content) {
        this.star = star;
        this.content = content;
    }

}
