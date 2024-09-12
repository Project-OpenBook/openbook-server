package com.openbook.openbook.domain.event;

import com.openbook.openbook.domain.booth.BoothArea;
import com.openbook.openbook.domain.event.dto.EventLayoutType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class EventLayout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EventLayoutType type;

    @OneToMany(mappedBy = "linkedEventLayout", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BoothArea> areas = new ArrayList<>();

    @Builder
    public EventLayout(String imageUrl, EventLayoutType type) {
        this.imageUrl = imageUrl;
        this.type = type;
    }
}
