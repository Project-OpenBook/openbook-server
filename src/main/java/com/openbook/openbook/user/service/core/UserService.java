package com.openbook.openbook.user.service.core;

import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.UserDTO;
import com.openbook.openbook.user.entity.dto.UserRole;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserOrException(final Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public User getAdminOrException() {
        return userRepository.findByRole(UserRole.ADMIN).orElseThrow(() ->
                new OpenBookException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmailOrException(final String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new OpenBookException(ErrorCode.USER_NOT_FOUND)
        );
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
