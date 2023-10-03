package com.smu.odowan.service;

import com.smu.odowan.config.security.JwtTokenProvider;
import com.smu.odowan.dto.TokenDTO;
import com.smu.odowan.dto.UserReq;
import com.smu.odowan.dto.UserRes;
import com.smu.odowan.entities.User;
import com.smu.odowan.global.BaseException;
import com.smu.odowan.global.BaseResponseStatus;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.smu.odowan.global.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
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
        }else if(userEntityOptional.isEmpty()){
        String encryptedPw = encoder.encode(user.getPassword());
        User newUser = User.builder()
                .email(user.getEmail())
                .password(encryptedPw)
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .challengeDoneCount(0L)
                .build();
        return new UserRes.UserJoinRes(userRepository.saveAndFlush(newUser));
        }else {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        }
    }


    public List<TokenDTO> login(UserReq.UserLoginReq request) throws BaseException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(FAILED_TO_LOGIN)); // 가입안된 이메일
        if(!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(FAILED_TO_LOGIN); // 비밀번호 일치 X
        }

        // 토큰 발급해서
        TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
        TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());

        // login 시 Redis에 RT:bareuni@email.com(key): --refresh token실제값--(value) 형태로 refresh 토큰 저장하기
        // opsForValue() : set을 통해 key,value값 저장하고 get(key)통해 value가져올 수 있음.
        // refreshToken.getTokenExpriresTime().getTime() : 리프레시 토큰의 만료시간이 지나면 해당 값 자동 삭제
        redisTemplate.opsForValue().set("RT:"+user.getEmail(),refreshToken.getToken(),refreshToken.getTokenExpiresTime().getTime(), TimeUnit.MILLISECONDS);

        List<TokenDTO> tokenDTOList = new ArrayList<>();
        tokenDTOList.add(refreshToken);
        tokenDTOList.add(accessToken);
        System.out.println(tokenDTOList);

        return tokenDTOList;
    }

    // 로그아웃 - userIdx
    public String logout(HttpServletRequest request) throws BaseException {

        Long userIdx = jwtTokenProvider.getCurrentUser(request);

        System.out.println("getCurrentUser()로 가져온 userIdx : "+userIdx);
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_USER_ID));

        // Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + user.getEmail()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + user.getEmail());
        }
        // 해당 AccessToken 유효시간 가지고 와서 BlackList 로 저장하기
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        Long expiration = jwtTokenProvider.getExpireTime(accessToken).getTime();
        // Redis 에 --accesstoken--(key) : logout(value) 로 저장, token 만료시간 지나면 자동 삭제
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return "로그아웃 성공";
    }

    // 토큰 재발급 - 요청 온 refresh token으로 access, refresh token을 모두 새로 발급한다
    public List<TokenDTO> reissue(HttpServletRequest request) throws BaseException {
        String rtk = request.getHeader("rtk");
        System.out.println("reissue함수실행 rtk: "+rtk);

        // refresh token 유효성 검증
        if (!jwtTokenProvider.validateToken(rtk))
            throw new BaseException(INVALID_JWT);

        String email = jwtTokenProvider.getTokenSub(rtk);
        System.out.println("rtk subject인 email: "+email);

        // Redis에서 email 기반으로 저장된 refresh token 값 가져오기
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + email);
        if(!refreshToken.equals(rtk)) {
            throw new BaseException(RTK_INCORRECT);
        }

        // refresh token 유효할 경우 새로운 토큰 생성
        List<TokenDTO> tokenDTOList = new ArrayList<>();
        TokenDTO newRefreshToken = jwtTokenProvider.createRefreshToken(email);
        TokenDTO newAccessToken = jwtTokenProvider.createAccessToken(email);
        tokenDTOList.add(newRefreshToken);
        tokenDTOList.add(newAccessToken);
        System.out.println("Access Token, Refresh Token 재발행: " +tokenDTOList);

        // Redis에 refresh token 업데이트
        redisTemplate.opsForValue().set("RT:"+email,newRefreshToken.getToken(),newRefreshToken.getTokenExpiresTime().getTime(),TimeUnit.MILLISECONDS);

        return tokenDTOList;
    }

    // 회원 탈퇴
    @Transactional
    public String deactivateUser(HttpServletRequest request) throws BaseException {
        Long userIdx = jwtTokenProvider.getCurrentUser(request);

        System.out.println("getCurrentUser()로 가져온 userIdx : "+userIdx);
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(USERS_EMPTY_USER_ID));

        // Redis에 로그인되어있는 토큰 삭제
        // Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + user.getEmail()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + user.getEmail());
        }
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        // 탈퇴한 토큰을 차단 (deactivateUser 토큰 블랙리스트)
        Long expiration = jwtTokenProvider.getExpireTime(accessToken).getTime();
        // Redis 에 --accesstoken--(key) : logout(value) 로 저장, token 만료시간 지나면 자동 삭제
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        userRepository.deleteById(user.getUserIdx());

        //시큐리티
        //SecurityContextHolder.clearContext();
        return "회원 탈퇴 성공";
    }

    // 비밀번호 발급 및 재설정
    @Transactional
    public String updatePassword(Long userIdx, String newPassword) throws BaseException{
        Optional<User> user = userRepository.findById(userIdx);
        user.ifPresent(u -> {
            u.setPassword(encoder.encode(newPassword));
            userRepository.saveAndFlush(u);
        });
        throw new BaseException(SUCCESS);
    }

    // 이메일 검증
    public boolean emailValidation(String email) throws BaseException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // 회원가입 시 이메일 중복 확인
    public Boolean emailCheck(UserReq.EmailCheckReq request) throws BaseException{
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return true; // 중복됨
        } else {
            throw new BaseException(POST_USERS_NOT_FOUND_EMAIL); // 중복 안됨 사용가능
        }
    }

    // 회원가입 시 닉네임 중복 확인
    public Boolean checkName(UserReq.NameCheckReq request) throws BaseException {
        Optional<User> optionalUser = userRepository.findByName(request.getName());

        if (optionalUser.isEmpty()) {
            throw new BaseException(POST_USERS_NOT_FOUND_NICKNAME); // 닉네임 사용가능
        } else {
            User user = optionalUser.get();
            return true; // 닉네임 중복됨
        }
    }

    public UserRes.UserInfo getUserInfo(HttpServletRequest request) throws BaseException {
        Long userIdx = jwtTokenProvider.getCurrentUser(request);
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(FAILED_TO_LOGIN));

        UserRes.UserInfo userInfo = new UserRes.UserInfo();
        userInfo.setEmail(user.getEmail());
        userInfo.setName(user.getName());
        userInfo.setAddress(user.getAddress());
        userInfo.setPhone(user.getPhone());
        return userInfo;
    }


    public BaseResponseStatus getEmPw(UserReq.EmailCheckReq emailCheckReq) throws Exception {
        User user = userRepository.findByEmail(emailCheckReq.getEmail())
                .orElseThrow(() -> new BaseException(FAILED_TO_LOGIN));
        String emPw = emailService.sendSimpleMessage(user.getEmail(), true);
        user.setPassword(encoder.encode(emPw));
        userRepository.saveAndFlush(user);
        return SUCCESS;
    }

    public boolean Emailverify(String email, String code) throws Exception {
        String storedCode = String.valueOf(redisTemplate.opsForValue().get("email:" + email));

        if (storedCode == null) {
            throw new Exception("인증번호를 다시 받아주세요.");
        }

        return storedCode.equals(code);
    }

    public boolean validatePw(String pw, HttpServletRequest request) throws Exception {
        Long userIdx = jwtTokenProvider.getCurrentUser(request);
        User user = userRepository.findById(userIdx).orElse(null);
        if (encoder.matches(pw, user.getPassword()))
            return true;
        else throw new Exception("비밀번호 불일치");
    }

    public Optional<User> changePw(UserReq.PasswordChangeReq changePwInfo, HttpServletRequest request) throws Exception {
        Long userIdx = jwtTokenProvider.getCurrentUser(request);
        User user = userRepository.findById(userIdx).orElse(null);
        String encryptedPw = encoder.encode(changePwInfo.getNewPw());
        if (changePwInfo.getNewPw().equals(changePwInfo.getNewPwCheck())) {
            user.setPassword(encryptedPw);
            return Optional.of(userRepository.saveAndFlush(user));
        }
        else throw new Exception("비밀번호 재확인");
    }

    public Optional<User> updateMyInfo(UserReq.UserChangeReq user, HttpServletRequest request) throws Exception {
        Long userIdx = jwtTokenProvider.getCurrentUser(request);
        User userEntity = userRepository.findById(userIdx).orElse(null);
        if (userEntity == null) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
        if (user.getName() != null) {
            userEntity.setName(user.getName());
        }
        if (user.getPhone() != null) {
            userEntity.setPhone(user.getPhone());
        }
        if (user.getAddress() != null) {
            userEntity.setAddress(user.getAddress());
        }

        return Optional.of(userRepository.saveAndFlush(userEntity));

    }

}
