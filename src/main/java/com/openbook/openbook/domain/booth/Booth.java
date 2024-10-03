package com.openbook.openbook.domain.booth;

import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.EntityBasicTime;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.booth.dto.BoothUpdateData;
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

    @OneToMany(mappedBy = "linkedBooth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothTag> boothTags = new ArrayList<>();

    @OneToMany(mappedBy = "linkedBooth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothReservation> boothReservations = new ArrayList<>();

    @OneToMany(mappedBy = "linkedBooth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothProductCategory> boothProductCategories = new ArrayList<>();

    @OneToMany(mappedBy = "linkedBooth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothNotice> boothNotices = new ArrayList<>();

    @OneToMany(mappedBy = "linkedBooth", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothReview> boothReviews = new ArrayList<>();

    @Override
    public void setPrePersist() {
        super.setPrePersist();
        this.status = BoothStatus.WAITING;
    }

    public void updateStatus(BoothStatus status){ this.status = status; }

    public void updateBooth(BoothUpdateData updateData){
        if(updateData.name() != null){
            this.name = updateData.name();
        }
        if(updateData.description() != null){
            this.description = updateData.description();
        }
        if(updateData.openTime() != null){
            this.openTime = updateData.openTime();
        }
        if(updateData.closeTime() != null){
            this.closeTime = updateData.closeTime();
        }
        if(updateData.mainImage() != null){
            this.mainImageUrl = updateData.mainImage();
        }
        if(updateData.accountBankName() != null){
            this.accountBankName = updateData.accountBankName();
        }
        if(updateData.accountNumber() != null){
            this.accountNumber = updateData.accountNumber();
        }

    }

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







