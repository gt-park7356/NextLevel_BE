package com.example.NextLevel.domain.member.dto.request;

import com.example.NextLevel.domain.member.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 무시
public class MemberUpdateByAdminRequest {

    private String email;

    private String address;

    private Role role;

    private String password;

}
