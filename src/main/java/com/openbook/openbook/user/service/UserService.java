package com.openbook.openbook.user.service;

import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.UserDTO;
import com.openbook.openbook.user.dto.UserRole;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserOrException(final Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmailOrException(final String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "이메일과 일치하는 유저가 존재하지 않습니다."));
    }

    public void createUser(UserDTO user){
        userRepository.save(
          User.builder()
                  .email(user.email())
                  .name(user.name())
                  .nickname(user.nickname())
                  .password(user.password())
                  .role(UserRole.USER)
                  .build()
        );
    }
}
