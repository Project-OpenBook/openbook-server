package com.openbook.openbook.user;


import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.SignUpRequest;
import com.openbook.openbook.user.dto.UserRole;
import com.openbook.openbook.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void signup(final SignUpRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new OpenBookException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        }
        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .nickname(request.nickname())
                .password(encoder.encode(request.password()))
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

}
