package com.smu.odowan.service;


import com.smu.odowan.config.security.JwtTokenProvider;
import com.smu.odowan.dto.RoleType;
import com.smu.odowan.dto.TokenDTO;
import com.smu.odowan.dto.UserReq;
import com.smu.odowan.dto.UserRes;
import com.smu.odowan.entities.User;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
        if(userEntityOptional.isPresent()){
            User userEntity = userEntityOptional.get();
            log.info(String.valueOf(userEntity.getStatus()));

            if ('D'==userEntity.getStatus()) {
                String encryptedPw = encoder.encode(user.getPassword());
                userEntity.setPassword(encryptedPw);
                userEntity.setStatus('A');
                return userRepository.saveAndFlush(userEntity);
            }
            else
                throw new Exception("이미 존재하는 이메일입니다.");
        }
        String encryptedPw = encoder.encode(user.getPassword());
        User newUser = User.builder()
                .email(user.getEmail())
                .password(encryptedPw)
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .local(user.getLocal())
                .status('A')
                .role(String.valueOf(RoleType.USER))
                .login_cnt(0L)
                .build();
        return userRepository.saveAndFlush(newUser);
    }

    public List<TokenDTO> login(UserDTO user) throws Exception {
        User userEntity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new Exception("가입되지 않은 이메일입니다."));
        if (!encoder.matches(user.getPassword(), userEntity.getPassword())) {
            throw new Exception("잘못된 비밀번호입니다.");
        }
        if (userEntity.getStatus() == 'D')
            throw new Exception("탈퇴된 사용자입니다.");
        userEntity.setLogin_at(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        userEntity.setLogin_cnt(userEntity.getLogin_cnt()+1);
        userRepository.saveAndFlush(userEntity);

        // token 발급
        TokenDTO refreshToken = jwtTokenProvider.createRefreshToken();
        TokenDTO accessToken = jwtTokenProvider.createAccessToken(userEntity.getEmail(), userEntity.getRole());

        // login 시 Redis 에 RT: user@email.com(key) : ----token-----(value) 형태로 token 저장
        redisTemplate.opsForValue().set("AT:"+userEntity.getEmail(), accessToken.getToken(), accessToken.getTokenExpiresTime().getTime(), TimeUnit.MILLISECONDS);
//        redisTemplate.opsForValue().set("RT:"+userEntity.getEmail(), refreshToken.getToken(), refreshToken.getTokenExpiresTime().getTime(), TimeUnit.MILLISECONDS);

        List<TokenDTO> tokenDTOList = new ArrayList<>();
        tokenDTOList.add(refreshToken);
        tokenDTOList.add(accessToken);
        return tokenDTOList;
    }

    public String logout(HttpServletRequest request) {
        try {
            // token 으로 user 정보 받음
            String email = jwtTokenProvider.getCurrentUser(request);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

            user.setLogin_cnt(0L);
            userRepository.saveAndFlush(user);

            // Redis 에서 해당 User email 로 저장된 token 이 있는지 확인 후 있는 경우 삭제
            Object token = redisTemplate.opsForValue().get("RT:" + user.getEmail());
            if (token != null) {
                redisTemplate.delete("RT:"+user.getEmail());
            }

            Long expire = jwtTokenProvider.getExpireTime((String) token).getTime();
            redisTemplate.opsForValue().set(token, "logout", expire, TimeUnit.MILLISECONDS);

            return "로그아웃 성공";
        } catch (Exception exception) {
//            return exception.getMessage();
            return "유효하지 않은 토큰입니다.";
        }
    }

    public boolean validatePw(String pw, HttpServletRequest request) throws Exception {
        String email = jwtTokenProvider.getCurrentUser(request);
        User userEntity = userRepository.findByEmail(email).orElse(null);
        if (encoder.matches(pw, userEntity.getPassword()))
            return true;
        else throw new Exception("비밀번호 불일치");
    }

    public Optional<User> changePw(ChangePwInfo changePwInfo, HttpServletRequest request) throws Exception {
        String email = jwtTokenProvider.getCurrentUser(request);
        User userEntity = userRepository.findByEmail(email).orElse(null);
        String encryptedPw = encoder.encode(changePwInfo.getNewPw());
        if (changePwInfo.getNewPw().equals(changePwInfo.getNewPwCheck())) {
            userEntity.setPassword(encryptedPw);
            return Optional.of(userRepository.saveAndFlush(userEntity));
        }
        else throw new Exception("비밀번호 재확인");
    }

    //랜덤 인증 코드 생성
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }
    public String findPw(UserDTO user) throws Exception {
        // 이메일 확인
        User userEntity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new Exception("가입되지 않은 이메일입니다."));
        // 이름 확인
        if (!userEntity.getName().equals(user.getName())) {
            throw new Exception("일치하는 사용자 정보가 없습니다.");
        }

//        Random r = new Random();
//        Integer num = r.nextInt(999999); // 랜덤 난수 설정

        String num = createCode();

        String setFrom = "munecting@gmail.com"; // 보내는 사람
        String toMail = userEntity.getEmail(); // 받는 사람
        String title = "[OdoWan] 비밀번호 변경 인증 메일입니다";
        String content = System.getProperty("line.separator") + "안녕하세요. OdoWan 입니다." + System.getProperty("line.separator")
                + "비밀번호 변경 코드는 " + num + " 입니다." + System.getProperty("line.separator");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            messageHelper.setFrom(setFrom);
            messageHelper.setTo(toMail);
            messageHelper.setSubject(title);
            messageHelper.setText(content);

            mailSender.send(message);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("findPw");
        mv.addObject("new_pw", num);

        log.info(String.valueOf(mv.getModel().get("new_pw")));

        String encryptedPw = encoder.encode(num);
        userEntity.setPassword(encryptedPw);
        userRepository.saveAndFlush(userEntity);

        return mv.getModel().get("new_pw").toString();
    }

    public String getNewAccessToken(String email, HttpServletRequest request) throws Exception {
        String rtk = request.getHeader("rtk");
        if (!jwtTokenProvider.validateToken(rtk))
            throw new Exception("유효하지 않은 refresh token 입니다. 재로그인이 필요합니다.");
        return jwtTokenProvider.createAccessToken(email, String.valueOf(RoleType.USER)).getToken();
    }

    public String mailCheck(UserDTO user) throws Exception {

//        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(user.getEmail());
//        if(userEntityOptional.isPresent())
//            throw new Exception("이미 존재하는 이메일입니다.");

        Random r = new Random();
        Integer num = r.nextInt(999999); // 랜덤 난수 설정

        String setFrom = "munecting@gmail.com"; // 보내는 사람
        String toMail = user.getEmail(); // 받는 사람
        String title = "[OdoWan] 이메일 인증 메일입니다";
        String content = System.getProperty("line.separator") + "안녕하세요. OdoWan 입니다." + System.getProperty("line.separator")
                + "이메일 인증번호는 " + num + " 입니다." + System.getProperty("line.separator");

        redisTemplate.opsForValue().set("email:"+user.getEmail(), num.toString(), 2 , TimeUnit.MINUTES);


        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            messageHelper.setFrom(setFrom);
            messageHelper.setTo(toMail);
            messageHelper.setSubject(title);
            messageHelper.setText(content);

            mailSender.send(message);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mailCheck");
        mv.addObject("mail_num", num);

        log.info(String.valueOf(mv.getModel().get("mail_num")));

        return mv.getModel().get("mail_num").toString();
    }



    public boolean verifyEmailCode(String email, String code) throws Exception {
        String storedCode = String.valueOf(redisTemplate.opsForValue().get("email:" + email));

        if (storedCode == null) {
            throw new Exception("인증번호를 다시 받아주세요.");
        }

        return storedCode.equals(code);
    }


}
