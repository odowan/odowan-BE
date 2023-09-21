package com.smu.odowan.service;


import com.smu.odowan.config.security.JwtTokenProvider;
import com.smu.odowan.dto.RoleType;
import com.smu.odowan.dto.TokenDTO;
import com.smu.odowan.entities.User;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialLoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RedisTemplate redisTemplate;
    public String socialLogin(String email, String name, String image) {
        Optional<User> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isPresent()) {

            User userEntity = optionalUserEntity.get();
            userEntity.setLogin_at(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
            userEntity.setLogin_cnt(userEntity.getLogin_cnt()+1);
            userRepository.saveAndFlush(userEntity);

            // token 발급
            TokenDTO token = jwtTokenProvider.createAccessToken(email, "USER");

            // Redis 에 RTL user@email.com(key) : ----token-----(value) 형태로 token 저장
            redisTemplate.opsForValue().set("AT:"+email, token.getToken(), token.getTokenExpiresTime().getTime(), TimeUnit.MILLISECONDS);
            return token.getToken();
        }
        else {
            String password = UUID.randomUUID().toString();
            User newUser = User.builder()
                    .email(email)
                    .nickname(name)
                    .password(password)
                    .local("서울 영등포구")
                    .status('A')
                    .role(String.valueOf(RoleType.USER))
                    .login_cnt(0L)
                    .created_at(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                    .build();
            return String.valueOf(userRepository.saveAndFlush(newUser));
        }
    }
}
