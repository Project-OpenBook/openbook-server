package com.openbook.openbook.domain.user;

import com.openbook.openbook.domain.user.dto.BookmarkType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private BookmarkType bookmarkType;

    private Long resourceId;

    private Boolean alarmSet;

    @Builder
    public Bookmark(User user, BookmarkType bookmarkType, Long resourceId, Boolean alarmSet) {
        this.user = user;
        this.bookmarkType = bookmarkType;
        this.resourceId = resourceId;
        this.alarmSet = alarmSet;
    }

}
