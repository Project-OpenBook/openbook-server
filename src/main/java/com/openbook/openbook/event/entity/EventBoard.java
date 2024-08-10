package com.openbook.openbook.event.entity;


import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.global.util.EntityBasicTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventBoard extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event linkedEvent;


    @Builder
    public EventBoard(String image, EventStatus type, Event linkedEvent) {
        this.image = image;
        this.type = type.name();
        this.linkedEvent = linkedEvent;
    }
}
