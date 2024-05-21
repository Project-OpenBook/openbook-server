package com.openbook.openbook.booth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth booth;

    private String name;
    private int supply;
    private int price;
    private String description;
    private String productImg;

    @Builder
    public BoothProduct(Booth booth, String name, int supply, int price, String description, String productImg){
        this.booth = booth;
        this.name = name;
        this.supply = supply;
        this.price = price;
        this.description = description;
        this.productImg = productImg;
    }
}
