package com.openbook.openbook.domain;


import static java.time.LocalDateTime.now;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityBasicTime {
    @CreatedDate
    private LocalDateTime registeredAt;

    @PrePersist
    public void setPrePersist() {
        this.registeredAt = now();
    }

}
