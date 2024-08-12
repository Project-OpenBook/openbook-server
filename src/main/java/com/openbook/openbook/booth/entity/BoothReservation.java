package com.openbook.openbook.booth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @Builder
    public BoothReservation(Booth linkedBooth, String content, LocalDate date){
        this.content = content;
        this.date = date;
        this.linkedBooth = linkedBooth;
    }
}
