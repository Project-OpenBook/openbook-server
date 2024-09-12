package com.openbook.openbook.domain.booth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @Builder
    public BoothTag(String name, Booth booth){
        this.name = name;
        this.linkedBooth = booth;
    }

}
