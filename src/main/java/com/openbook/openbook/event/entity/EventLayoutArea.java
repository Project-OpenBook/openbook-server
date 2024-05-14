package com.openbook.openbook.event.entity;

import com.openbook.openbook.booth.entity.BoothLocation;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventLayoutArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventLayout linkedEventLayout;

    @Enumerated(EnumType.STRING)
    private EventLayoutAreaStatus status;

    private String type;

    private String number;

    @OneToMany(mappedBy = "eventLayoutArea", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothLocation> locations = new ArrayList<>();

    @PrePersist
    public void setFirstEventLayoutAreaStatus() {
        this.status = EventLayoutAreaStatus.EMPTY;
    }

    @Builder
    public EventLayoutArea(EventLayout linkedEventLayout, String type, String number) {
        this.linkedEventLayout = linkedEventLayout;
        this.type = type;
        this.number = number;
    }
}