package com.openbook.openbook.domain.user;

import com.openbook.openbook.domain.EntityBasicTime;
import jakarta.persistence.Entity;
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
public class Alarm extends EntityBasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    private String alarmType;

    private String content;

    private String message;

    @Builder
    public Alarm(User receiver, User sender, String alarmType, String content, String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.alarmType = alarmType;
        this.content = content;
        this.message = message;
    }
}
