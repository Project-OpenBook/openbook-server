package com.openbook.openbook.domain.booth;

import com.openbook.openbook.service.booth.dto.BoothReservationUpdateData;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate date;

    private String imageUrl;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @OneToMany(mappedBy = "linkedReservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothReservationDetail> boothReservationDetails = new ArrayList<>();

    @Builder
    public BoothReservation(String name, String description, Booth linkedBooth, LocalDate date, String imageUrl, int price){
        this.name = name;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
        this.price = price;
        this.linkedBooth = linkedBooth;
    }

    public void updateReservation(BoothReservationUpdateData updateData){
        if(updateData.name() != null){
            this.name = updateData.name();
        }
        if(updateData.description() != null){
            this.description = updateData.description();
        }
        if(updateData.image() != null){
            this.imageUrl = updateData.image();
        }
        if(updateData.price() != null){
            this.price = updateData.price();
        }
        if(updateData.date() != null){
            this.date = updateData.date();
        }
    }
}
