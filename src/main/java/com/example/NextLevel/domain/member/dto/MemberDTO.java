package com.example.NextLevel.domain.member.dto;

import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class MemberDTO {
    private String username;
    private String password;
    private String email;
    private Role role;
    private String profileImageUrl;


    public MemberDTO(Member member) {
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.profileImageUrl = "/local_image_storage/" + member.getProfileImageUrl();
    }

    @JsonIgnore
    public Map<String, Object> getPayload() {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("username", username);
        payloadMap.put("email", email);
        payloadMap.put("role", role);
        return payloadMap;
    }
}
