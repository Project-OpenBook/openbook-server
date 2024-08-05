package com.openbook.openbook.booth.entity;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.event.entity.EventLayout;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventLayout linkedEventLayout;

    @Enumerated(EnumType.STRING)
    private BoothAreaStatus status;

    private String classification;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @PrePersist
    public void setFirstEventLayoutAreaStatus() {
        this.status = BoothAreaStatus.EMPTY;
    }

    @Builder
    public BoothArea(EventLayout linkedEventLayout, String classification, String number) {
        this.linkedEventLayout = linkedEventLayout;
        this.classification = classification;
        this.number = number;
    }

    public void updateBooth(BoothAreaStatus status, Booth booth){
        this.status = status;
        this.linkedBooth = booth;
    }

    public void deleteBooth(BoothAreaStatus status){
        this.status = status;
        this.linkedBooth = null;
    }

    public void updateStatus(BoothAreaStatus status){
        this.status = status;
    }
}
