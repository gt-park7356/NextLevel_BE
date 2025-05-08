package com.example.NextLevel.domain.member.service;

import com.example.NextLevel.common.upload.ImageRepository;
import com.example.NextLevel.domain.member.dto.MemberDTO;
import com.example.NextLevel.domain.member.dto.request.MemberSignUpRequest;
import com.example.NextLevel.domain.member.dto.request.MemberUpdateByAdminRequest;
import com.example.NextLevel.domain.member.dto.request.MemberUpdateRequest;
import com.example.NextLevel.domain.member.dto.response.MemberInfoByAdminResponse;
import com.example.NextLevel.domain.member.dto.response.MemberInfoResponse;
import com.example.NextLevel.domain.member.exception.MemberException;
import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;

    @Value("${file.local.upload.path}")
    private String uploadPath;

    //회원 가입
    @Transactional
    public void signUp(final MemberSignUpRequest request, final MultipartFile profileImage) {
        // 중복 체크
        if (memberRepository.existsByUsername(request.getUsername())) { throw MemberException.DUPLICATE_ID.get(); }
        if (memberRepository.existsByEmail(request.getEmail())) { throw MemberException.DUPLICATE_EMAIL.get(); }

        // 프로필 이미지 처리
        String imageUrl = (profileImage != null && !profileImage.isEmpty())
                ? imageRepository.upload(profileImage)
                : "defaultImage.png";

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        memberRepository.save(request.toEntity(encodedPassword, imageUrl));
    }

    @Transactional
    public void updateMember(String userId, MemberUpdateRequest request) {
        Member foundMember = memberRepository.findByUsername(userId)
                .orElseThrow(MemberException.NOT_FOUND::get);

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(request.getEmail())) { throw MemberException.DUPLICATE_EMAIL.get(); }

        if(request.getEmail() != null) { foundMember.changeEmail(request.getEmail()); }
    }

    public MemberDTO findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);
        return new MemberDTO(member);
    }

    public void verifyCurrentPassword(String username, String currentPassword) {
        // 사용자 조회
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberException.NOT_FOUND::get);

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw MemberException.NOT_MATCHED_PASSWORD.get();
        }
    }

    @Transactional  // 비밀번호 변경
    public void updatePassword(String username, String newPassword) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        // 새 비밀번호 변경
        member.changePassword(newPassword, passwordEncoder);

    }


    @Transactional  // 프로필 사진 변경
    public String updateProfileImage(String username, MultipartFile image) {
        // 사용자 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        // 이미지가 제출되지 않았다면, 기존 이미지를 유지
        if (image == null && image.isEmpty()) {
            return null;
        }
        // 파일 업로드
        String imageUrl = imageRepository.upload(image);
        // 이미지 URL을 사용자 프로필에 업데이트
        member.changeProfileImageUrl(imageUrl);

        return "/local_image_storage/" + imageUrl;


    }

    @Transactional        // 회원 탈퇴
    public void deleteMember(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        memberRepository.delete(user);
    }

    public MemberInfoResponse getUserInfo(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);
        return new MemberInfoResponse(member, uploadPath);
    }

    public Page<MemberInfoByAdminResponse> getAllUsersByAdmin(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberInfoByAdminResponse::new);
    }

    public MemberInfoByAdminResponse getUserInfoByAdmin(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);
        return new MemberInfoByAdminResponse(user);
    }

    @Transactional
    public void updateUserByAdmin(String username, MemberUpdateByAdminRequest request) {
        Member foundMember = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);

        // 요청바디에 있는 필드만 수정
        if(request.getEmail() != null) { foundMember.changeEmail(request.getEmail()); }
        if(request.getRole() != null) { foundMember .changeRole(request.getRole()); }
        if(request.getPassword() != null) { foundMember.changePassword(request.getPassword(), passwordEncoder); }
    }

    //UserId로 찾고 Member를 반환
    public Member findUserByUsername(String username) {
            Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberException.NOT_FOUND::get);
        return member;
    }
}
