package com.example.NextLevel.domain.member.dto.request;

import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;


@Getter
public class MemberSignUpRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;


    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^(?=.{1,100}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;


    private String profileImageUrl;



    public Member toEntity(String encodedPassword, String profileImageUrl) {
        return Member.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER) //등록시에 USER로 자동설정
                .profileImageUrl(profileImageUrl)
                .build();
    }


}
