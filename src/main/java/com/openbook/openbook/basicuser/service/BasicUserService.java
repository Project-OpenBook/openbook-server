package com.openbook.openbook.basicuser.service;


import com.openbook.openbook.global.util.JwtUtils;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.repository.UserRepository;
import com.openbook.openbook.basicuser.dto.request.LoginRequest;
import com.openbook.openbook.basicuser.dto.request.SignUpRequest;
import com.openbook.openbook.user.dto.UserRole;
import com.openbook.openbook.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

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

    @Transactional(readOnly = true)
    public String login(final LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "이메일과 일치하는 유저가 존재하지 않습니다."));
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
        return jwtUtils.generateToken(user.getId());
    }
}
