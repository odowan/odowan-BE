package com.smu.odowan.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity(name = "User")
@Table(name = "User")
//@SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// , generator = "user_sequence"
    private Long userIdx;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "nickname", nullable = true)
    private String nickname;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "local", nullable = true)
//    @ColumnDefault("프로필을 입력해주세요")
    private String local;

//    @Column(name = "all_replyCnt", nullable = false)
//    @ColumnDefault("0")
//    private String all_replyCnt;

    @Column(name = "profileImage", nullable = true)
    private String profileImage;

    @Column(name = "status", nullable = false)
    @ColumnDefault("'A'") // A: 활성 유저 D: 탈퇴 유저
    private char status;

    @Column(name = "role", nullable = false) // User, Guest
    private String role;

    @Column(name = "created_at", nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updated_at;

    @Column(name = "login_at", nullable = true)
    private LocalDateTime login_at;

    @Column(name = "login_cnt", nullable = true)
    @ColumnDefault("0")
    private Long login_cnt;

    @PrePersist
    public void create_at(){
        this.created_at = LocalDateTime.now();
    }


//    public UserDTO toDTO() {
//        return UserDTO.builder()
//                .email(email)
//                .password(password)
//                .name(name)
//                .phone(phone)
//                .local(local)
//                .profileImage(profileImage)
//                .status(status)
//                .role(role)
//                .created_at(created_at)
//                .updated_at(updated_at)
//                .login_at(login_at)
//                .login_cnt(login_cnt)
//                .build();
//    }

    // UserDetails 상속
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}