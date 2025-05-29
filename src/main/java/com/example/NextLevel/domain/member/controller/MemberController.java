package com.example.NextLevel.domain.member.controller;

import com.example.NextLevel.common.response.ApiResponse;
import com.example.NextLevel.domain.member.dto.MemberDTO;
import com.example.NextLevel.domain.member.dto.request.MemberSignUpRequest;
import com.example.NextLevel.domain.member.dto.request.MemberUpdateByAdminRequest;
import com.example.NextLevel.domain.member.dto.request.MemberUpdateRequest;
import com.example.NextLevel.domain.member.dto.response.MemberInfoByAdminResponse;
import com.example.NextLevel.domain.member.dto.response.MemberInfoResponse;
import com.example.NextLevel.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup") //회원가입
    public ResponseEntity<ApiResponse> signUp(@RequestPart("request") @Valid final MemberSignUpRequest request,
                                              @RequestPart(value = "image", required = false) final MultipartFile profileImage) {
        memberService.signUp(request, profileImage);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/me")  // 내 정보 조회
    public ResponseEntity<ApiResponse> getCurrentUser(Authentication authentication) {
        String username = authentication.getName(); // 인증된 사용자 ID 가져오기
        MemberDTO memberDTO = memberService.findByUsername(username); // 사용자 정보 조회
        return ResponseEntity.ok(ApiResponse.success(memberDTO)); // 사용자 정보를 포함한 ApiResponse 반환
    }

    @GetMapping("{username}")  // 다른 회원 정보 조회
    public ResponseEntity<ApiResponse> getUserInfo(@PathVariable("username") final String username) {
        MemberInfoResponse memberInfo = memberService.getUserInfo(username);
        return ResponseEntity.ok(ApiResponse.success(memberInfo));
    }

    @PatchMapping("/me")   // 내 정보 수정
    public ResponseEntity<ApiResponse> updateCurrentUser(@RequestBody @Valid MemberUpdateRequest request,
                                                                             Authentication authentication) {
        String username = authentication.getName();
        memberService.updateMember(username, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/me/verify-password")   // 현재 비밀번호가 맞는지 확인
    public ResponseEntity<ApiResponse> verifyPassword(@RequestParam String currentPassword, Authentication authentication) {
        String userId = authentication.getName();
        memberService.verifyCurrentPassword(userId, currentPassword);

        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PutMapping("/me/change-password") // 비밀번호 변경
    public ResponseEntity<ApiResponse> changePassword(@RequestParam String newPassword, Authentication authentication)
    {
        String username = authentication.getName();
        memberService.updatePassword(username, newPassword);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping(value = "/me/profileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)   // 프로필 사진 수정
    public ResponseEntity<ApiResponse> modifyProfileImage(
            @RequestPart(value = "image", required = false) final MultipartFile image,
            Authentication authentication
    ) {
        String username = authentication.getName();
        String imageUrl = memberService.updateProfileImage(username, image);
        return ResponseEntity.ok(ApiResponse.success(imageUrl));
    }

    @DeleteMapping("/me")  // 회원 탈퇴
    public ResponseEntity<ApiResponse> deleteCurrentMember(Authentication authentication) {
        String username = authentication.getName();
        memberService.deleteMember(username);  // 사용자 삭제 서비스 호출
        return ResponseEntity.ok(ApiResponse.success(null));  // 성공 메시지 반환
    }

    //------------------------- ADMIN 권한 기능 -----------------------------------------

    @GetMapping("/admin/member-list")  // 회원 목록 조회
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MemberInfoByAdminResponse> members = memberService.getAllUsersByAdmin(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    @GetMapping("/admin/{username}")   // 특정 사용자 정보 조회
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MemberInfoByAdminResponse>> getMemberInfoByAdmin(@PathVariable("username") String username) {
        MemberInfoByAdminResponse userInfo = memberService.getUserInfoByAdmin(username);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }


    @PatchMapping("/admin/{username}")  // 회원 정보 수정
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateMemberInfoByAdmin(
            @PathVariable("username") String username,
            @RequestBody final MemberUpdateByAdminRequest request
    ) {
        memberService.updateUserByAdmin(username, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping(value = "/admin/profileImage/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)   // 회원 프로필 사진 수정
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> modifyProfileImage(
            @PathVariable("username") String username,
            @RequestPart(value = "image", required = false) final MultipartFile image
    ) {
        String imageUrl = memberService.updateProfileImage(username, image);
        return ResponseEntity.ok(ApiResponse.success(imageUrl));
    }

    @DeleteMapping("/admin/{username}")  // 회원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUserByAdmin(@PathVariable("username") String username) {
        memberService.deleteMember(username);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
