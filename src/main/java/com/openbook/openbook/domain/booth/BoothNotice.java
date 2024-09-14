package com.openbook.openbook.domain.booth;

import com.openbook.openbook.domain.booth.dto.BoothNoticeType;
import com.openbook.openbook.domain.EntityBasicTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothNotice extends EntityBasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    private String content;

    private String type;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booth linkedBooth;

    @Builder
    public BoothNotice(String title, String content, BoothNoticeType type, String imageUrl, Booth linkedBooth){
        this.title = title;
        this.content = content;
        this.type = type.name();
        this.imageUrl = imageUrl;
        this.linkedBooth = linkedBooth;
    }
}
