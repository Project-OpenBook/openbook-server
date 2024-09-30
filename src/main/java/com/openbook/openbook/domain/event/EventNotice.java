package com.openbook.openbook.domain.event;


import com.openbook.openbook.domain.event.dto.EventNoticeType;
import com.openbook.openbook.domain.EntityBasicTime;
import com.openbook.openbook.service.event.dto.EventNoticeUpdateData;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventNotice extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    private String content;

    private String type;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event linkedEvent;

    @Builder
    public EventNotice(String title, String content, EventNoticeType type, String imageUrl, Event linkedEvent) {
        this.title = title;
        this.content = content;
        this.type = type.name();
        this.imageUrl = imageUrl;
        this.linkedEvent = linkedEvent;
    }

    public void updateNotice(EventNoticeUpdateData updateData) {
        if(updateData.title()!=null) {
            this.title = updateData.title();
        }
        if(updateData.content()!=null) {
            this.content = updateData.content();
        }
        if (updateData.type()!=null) {
            this.type = updateData.type().name();
        }
        if (updateData.imageUrl()!=null) {
            this.imageUrl = updateData.imageUrl();
        }
    }

}
