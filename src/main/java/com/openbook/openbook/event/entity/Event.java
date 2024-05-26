package com.openbook.openbook.event.entity;

import com.openbook.openbook.event.dto.EventStatus;
import com.openbook.openbook.global.util.EntityBasicTime;
import com.openbook.openbook.user.entity.User;
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
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User manager;

    @OneToOne
    private EventLayout layout;

    private String location;

    private String name;

    private String mainImageUrl;

    private String description;

    private LocalDate openDate;

    private LocalDate closeDate;

    private LocalDate boothRecruitmentStartDate;

    private LocalDate boothRecruitmentEndDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventTagDetail> tagDetails = new ArrayList<>();

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = EventStatus.WAITING;
    }

    public void updateStatus(EventStatus status) {
        this.status = status;
    }

    @Builder
    public Event(User manager, EventLayout layout, String location, String name, String mainImageUrl,
                 String description, LocalDate openDate, LocalDate closeDate, LocalDate boothRecruitmentStartDate,
                 LocalDate boothRecruitmentEndDate) {
        this.manager = manager;
        this.layout = layout;
        this.location = location;
        this.name = name;
        this.mainImageUrl = mainImageUrl;
        this.description = description;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.boothRecruitmentStartDate = boothRecruitmentStartDate;
        this.boothRecruitmentEndDate = boothRecruitmentEndDate;
    }
}
