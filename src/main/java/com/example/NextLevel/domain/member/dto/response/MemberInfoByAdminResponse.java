package com.example.NextLevel.domain.member.dto.response;

import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.model.enums.Role;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberInfoByAdminResponse {
    private final String username;
    private final String email;
    private final Role role;
    private final String profileImage;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MemberInfoByAdminResponse(final Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.profileImage = "/local_image_storage/" + member.getProfileImageUrl();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}

