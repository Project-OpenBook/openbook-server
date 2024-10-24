package com.openbook.openbook.service.user;

import com.openbook.openbook.api.user.request.UserModifyRequest;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.user.dto.UserProfileDto;
import com.openbook.openbook.service.user.dto.UserUpdateDto;
import com.openbook.openbook.util.TokenProvider;
import com.openbook.openbook.api.user.request.LoginRequest;
import com.openbook.openbook.api.user.request.SignUpRequest;
import com.openbook.openbook.domain.user.dto.UserRole;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.repository.user.UserRepository;
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
        return tokenProvider.generateToken(user.getId(), user.getNickname(), user.getRole().name());
    }

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId){
        User user = getUserOrException(userId);
        return UserProfileDto.of(user);
    }

    @Transactional
    public void modifyUser(long userId, UserModifyRequest request){
        User user = getUserOrException(userId);
        user.updateUser(UserUpdateDto.builder()
                        .name(request.name())
                        .nickname(request.nickname())
                        .email(request.email())
                        .build());
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
