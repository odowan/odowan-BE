package com.smu.odowan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender emailSender;

    private final RedisTemplate redisTemplate;

    //TODO : 비밀번호 임시 발급

    private MimeMessage createMessage(String to, String ePw, boolean isPwd) throws Exception {
        System.out.println("보내는 대상 : " + to);
        System.out.println((isPwd ? "임시 비밀번호 : " : "인증 코드 : ") + ePw);
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, to); // 보내는 대상
        message.setSubject("odowan " + (isPwd ? "임시 비밀번호 " : "인증 코드 ") + "발급입니다."); // 제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> odowan " + (isPwd ? "임시 비밀번호" : "인증 코드") + "입니다. </h1>"; // 동적으로 메시지 내용 변경
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>" + (isPwd ? "임시 비밀번호" : "인증 코드") + "</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += (isPwd ? "임시 비밀번호" : "인증 코드") + " : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress("odowan_team", "odowan")); // 보내는 사람

        redisTemplate.opsForValue().set("email:"+to, ePw, 2 , TimeUnit.MINUTES);

        return message;
    }


    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 비밀번호 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
    public String sendSimpleMessage(String to, boolean isPwd)throws Exception {
        String ePw = createKey();
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to, ePw, isPwd);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
}
