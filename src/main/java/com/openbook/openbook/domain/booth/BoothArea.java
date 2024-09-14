package com.openbook.openbook.domain.booth;

import com.openbook.openbook.domain.booth.dto.BoothAreaStatus;
import com.openbook.openbook.domain.event.EventLayout;
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

    public void updateStatus(Booth booth, BoothAreaStatus status){
        this.linkedBooth = booth;
        this.status = status;
    }

}
