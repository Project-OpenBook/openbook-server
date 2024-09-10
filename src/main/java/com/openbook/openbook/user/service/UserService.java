package com.openbook.openbook.user.service;

import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.TokenProvider;
import com.openbook.openbook.user.controller.request.LoginRequest;
import com.openbook.openbook.user.controller.request.SignUpRequest;
import com.openbook.openbook.user.entity.dto.UserRole;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
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

    @Transactional
    public void signup(final SignUpRequest request) {
        if(getUserByEmail(request.email()).isPresent()) {
            throw new OpenBookException(ErrorCode.DUPLICATE_EMAIL);
        }
        userRepository.save(User.builder()
                .email(request.email())
                .name(request.name())
                .nickname(request.nickname())
                .password(encoder.encode(request.password()))
                .role(UserRole.USER)
                .build()
        );
    }

    @Transactional(readOnly = true)
    public String login(final LoginRequest request) {
        User user = getUserByEmailOrException(request.email());
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new OpenBookException(ErrorCode.INVALID_PASSWORD);
        }
        return tokenProvider.generateToken(user.getId());
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmailOrException(final String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new OpenBookException(ErrorCode.USER_NOT_FOUND)
        );
    }


}
