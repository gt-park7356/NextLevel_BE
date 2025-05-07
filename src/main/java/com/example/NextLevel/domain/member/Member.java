package com.example.NextLevel.domain.member;

import com.example.NextLevel.domain.member.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id", nullable = false)
    private Long id;

    private String username;

    private String email;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;



    private String password;
}
