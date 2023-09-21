package com.smu.odowan.service;


import com.smu.odowan.config.security.JwtTokenProvider;
import com.smu.odowan.entities.User;
import com.smu.odowan.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisTemplate redisTemplate;


    public Optional<User> getMyInfo(HttpServletRequest request) throws Exception {
        return userRepository.findByEmail(jwtTokenProvider.getCurrentUser(request));
    }

    public ArrayList<User> all_user(HttpServletRequest request) throws Exception {
        return userRepository.findAll();
    }

    public Optional<User> updateMyInfo(UserDTO user, HttpServletRequest request) throws Exception {
        String email = jwtTokenProvider.getCurrentUser(request);
        User userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity == null) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
        if (user.getName() != null) {
            userEntity.setName(user.getName());
        }
        if (user.getProfileImage() != null) {
            userEntity.setProfileImage(user.getProfileImage());
        }
        if (user.getLocal() != null) {
            userEntity.setLocal(user.getLocal());
        }

        return Optional.of(userRepository.saveAndFlush(userEntity));

    }

    public Optional<User> deactivateUser(HttpServletRequest request) throws Exception {
        String email = jwtTokenProvider.getCurrentUser(request);
        User userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity == null) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }

        // Redis 에서 로그인되어 있는 토큰 삭제
        Object token = redisTemplate.opsForValue().get("AT:" + email);
        if (token != null) {
            redisTemplate.delete("AT:"+email);
        }
        // 탈퇴한 토큰 차단
        Long expire = jwtTokenProvider.getExpireTime((String) token).getTime();
        redisTemplate.opsForValue().set(token, "logout", expire, TimeUnit.MILLISECONDS);

        userEntity.setStatus('D');
        return Optional.of(userRepository.saveAndFlush(userEntity));
    }
}
