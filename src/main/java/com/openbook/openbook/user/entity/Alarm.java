package com.openbook.openbook.user.entity;

import com.openbook.openbook.global.util.EntityBasicTime;
import com.openbook.openbook.user.dto.AlarmType;
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
public class Alarm extends EntityBasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    @Builder
    public Alarm(User receiver, User sender, AlarmType type) {
        this.receiver = receiver;
        this.sender = sender;
        this.type = type;
    }
}
