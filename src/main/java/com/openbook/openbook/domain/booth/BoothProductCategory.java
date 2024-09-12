package com.openbook.openbook.domain.booth;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @OneToMany(mappedBy = "linkedCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothProduct> boothProducts = new ArrayList<>();

    @Builder
    public BoothProductCategory(String name, String description, Booth linkedBooth) {
        this.name = name;
        this.description = description;
        this.linkedBooth = linkedBooth;
    }
}
