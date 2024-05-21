package com.openbook.openbook.booth.entity;

import com.openbook.openbook.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothReservationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoothReservation boothReservation;

    private LocalDateTime time;

    @Builder
    public BoothReservationDetail(User user, BoothReservation boothReservation, LocalDateTime time){
        this.user = user;
        this.boothReservation = boothReservation;
        this.time = time;
    }
}
