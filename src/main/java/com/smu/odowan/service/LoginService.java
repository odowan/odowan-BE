package com.smu.odowan.service;

import com.smu.odowan.config.security.JwtTokenProvider;
import com.smu.odowan.dto.UserDTO;
import com.smu.odowan.dto.UserReq;
import com.smu.odowan.dto.UserRes;
import com.smu.odowan.entities.User;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserRes.UserJoinRes join(UserReq.UserJoinReq user) throws Exception {
        if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$"))
            throw new Exception("이메일 형식이 맞지 않습니다.");
//        if (!user.getPassword().matches("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%*^&+=]).{8,}$"))
//            throw new Exception("숫자, 영문자, 특수문자가 모두 포함된 8자리 이상의 비밀번호로 설정해 주세요.");
        Optional<User> userEntityOptional = userRepository.findByEmail(user.getEmail());
        if (userEntityOptional.isPresent()) {
            User userEntity = userEntityOptional.get();
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        String encryptedPw = encoder.encode(user.getPassword());
        User newUser = User.builder()
                .email(user.getEmail())
                .password(encryptedPw)
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
        return new UserRes.UserJoinRes(userRepository.saveAndFlush(newUser));
    }
}
