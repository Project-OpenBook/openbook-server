package com.openbook.openbook.booth.entity;

import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private BoothReservation linkedReservation;

    private String time;

    @Enumerated(EnumType.STRING)
    private BoothReservationStatus status;

    @PrePersist
    public void setFirstReservationStatus() {
        this.status = BoothReservationStatus.EMPTY;
    }

    @Builder
    public BoothReservationDetail(BoothReservation boothReservation, String time){
        this.linkedReservation = boothReservation;
        this.time = time;
    }
}
