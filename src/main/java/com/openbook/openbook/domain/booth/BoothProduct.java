package com.openbook.openbook.domain.booth;

import com.openbook.openbook.service.booth.dto.BoothProductUpdateData;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    private String name;

    private String description;

    private int stock;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoothProductCategory linkedCategory;

    @OneToMany(mappedBy = "linkedProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothProductImage> productImages = new ArrayList<>();

    @Builder
    public BoothProduct(String name, String description, int stock, int price, BoothProductCategory linkedCategory) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.linkedCategory = linkedCategory;
    }

    public void updateProduct(BoothProductUpdateData updateData) {
        if (updateData.name()!=null) {
            this.name = updateData.name();
        }
        if (updateData.description()!=null) {
            this.description = updateData.description();
        }
        if (updateData.stock()!=null) {
            this.stock = updateData.stock();
        }
        if (updateData.price()!=null) {
            this.price = updateData.price();
        }
    }

    public void updateCategory(BoothProductCategory category) {
        this.linkedCategory = category;
    }

}
