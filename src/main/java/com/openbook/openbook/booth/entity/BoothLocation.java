package com.openbook.openbook.booth.entity;


import com.openbook.openbook.event.entity.EventLayoutArea;
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
public class BoothLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth booth;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventLayoutArea eventLayoutArea;

    @Builder
    public BoothLocation(Booth booth, EventLayoutArea eventLayoutArea) {
        this.booth = booth;
        this.eventLayoutArea = eventLayoutArea;
    }
}
