package com.openbook.openbook.domain.user;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.EntityBasicTime;
import com.openbook.openbook.domain.user.dto.UserRole;
import com.openbook.openbook.service.user.dto.UserUpdateDto;
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
public class User extends EntityBasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String email;

    private String name;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booth> booths = new ArrayList<>();

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Alarm> alarms = new ArrayList<>();

    @Builder
    public User(String password, String email, String name, String nickname, UserRole role) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
    }

    public void updateUser(UserUpdateDto updateDto){
        if(updateDto.name() != null){
            this.name = updateDto.name();
        }
        if(updateDto.nickname() != null){
            this.nickname = updateDto.nickname();
        }
        if(updateDto.email() != null){
            this.email = updateDto.email();
        }
    }
}
