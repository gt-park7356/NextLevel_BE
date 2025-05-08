package com.example.NextLevel.domain.member.model;

import com.example.NextLevel.common.BaseTimeEntity;
import com.example.NextLevel.domain.member.model.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;


    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(final String username, final String password, final String email, final String profileImageUrl, final Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    public void changeProfileImageUrl(final String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }


    public void changeRole(Role role) {
        this.role = role;
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);  // 비밀번호 암호화 후 변경
    }

    public void changeEmail(String email) {
        this.email = email;
    }

}
