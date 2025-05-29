package com.example.NextLevel.domain.member.dto.response;


import com.example.NextLevel.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoResponse {
    private final String username;
    private final String email;
    private final String profileImageUrl;

    public MemberInfoResponse(final Member member, final String uploadPath) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.profileImageUrl = "/local_image_storage/" + member.getProfileImageUrl();  // 경로 + 파일명
    }
}