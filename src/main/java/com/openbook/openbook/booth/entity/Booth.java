package com.openbook.openbook.booth.entity;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.event.entity.Event;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booth extends EntityBasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event linkedEvent;

    private String name;

    private String description;

    private String mainImageUrl;

    private String accountNumber;

    private String accountBankName;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    @Enumerated(EnumType.STRING)
    private BoothStatus status;

    @OneToMany(mappedBy = "booth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothLocation> locations = new ArrayList<>();

    @OneToMany(mappedBy = "booth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothTagDetail> tagDetails = new ArrayList<>();

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = BoothStatus.WAITING;
    }

    public void updateStatus(BoothStatus status){ this.status = status; }

    @Builder
    public Booth(User manager, Event linkedEvent, String name, String description, String mainImageUrl,
                 String accountBankName, String accountNumber, LocalDateTime openTime, LocalDateTime closeTime) {
        this.manager = manager;
        this.linkedEvent = linkedEvent;
        this.name = name;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.accountBankName = accountBankName;
        this.accountNumber = accountNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}







