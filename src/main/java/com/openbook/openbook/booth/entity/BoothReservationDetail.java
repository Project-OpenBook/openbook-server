package com.openbook.openbook.booth.entity;

import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.global.util.EntityBasicTime;
import com.openbook.openbook.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothReservationDetail extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoothReservation boothReservation;

    private LocalDate date;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private BoothReservationStatus status;

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = BoothReservationStatus.WAITING;
    }

    @Builder
    public BoothReservationDetail(User user, BoothReservation boothReservation, LocalDate date, LocalDateTime time){
        this.user = user;
        this.boothReservation = boothReservation;
        this.date = date;
        this.time = time;
    }
}
