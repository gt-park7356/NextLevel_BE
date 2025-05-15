package com.example.NextLevel.domain.post.problem.service;

import com.example.NextLevel.common.upload.ProblemPostDataRepository;
import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.service.MemberService;
import com.example.NextLevel.domain.post.exception.PostException;
import com.example.NextLevel.domain.post.problem.dto.request.ProblemPostRequest;
import com.example.NextLevel.domain.post.problem.dto.response.ProblemPostResponse;
import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import com.example.NextLevel.domain.post.problem.repository.ProblemPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProblemPostServiceImpl implements ProblemPostService {
    private final ProblemPostRepository problemPostRepository;
    private final ProblemPostDataRepository problemPostDataRepository;
    private final MemberService memberService;

    @Transactional
    @Override
    public ProblemPostResponse register(ProblemPostRequest request, MultipartFile problemData, String username) {
        try{
            // 파일 업로드
            if (problemData != null && !problemData.isEmpty()) {
                String problemPostDataName = problemPostDataRepository.upload(problemData);
                request.setProblemData(problemPostDataName);
            }

            //사용자 검사 및 설정
            Member member = memberService.findUserByUsername(username);
            ProblemPost savedProblemPost = request.toEntity(member);

            problemPostRepository.save(savedProblemPost);
            return new ProblemPostResponse(savedProblemPost);
        } catch (Exception e){
            throw PostException.NOT_FOUND_EXCEPTION.getTaskException();
        }
    }

    //게시글 상세 조회
    @Override
    public ProblemPostResponse read(Long postId){
        ProblemPost foundPost = problemPostRepository.findById(postId).orElseThrow(PostException.NOT_FOUND_EXCEPTION::getTaskException);
        return new ProblemPostResponse(foundPost);
    }

    @Override
    public Page<ProblemPostResponse> getAllPosts(Pageable pageable) {
        return problemPostRepository.findAll(pageable).map(ProblemPostResponse::new);
    }

    @Override
    public Page<ProblemPostResponse> search(String title, String professorName, String subject, String school, Pageable pageable) {
        // null이면 빈 문자열로 치환
        String t = title == null ? "" : title;
        String p = professorName == null ? "" : professorName;
        String s = subject == null ? "" : subject;
        String sch = school == null ? "" : school;

        Page<ProblemPost> page = problemPostRepository.findByTitleContainingIgnoreCaseAndProfessorNameContainingIgnoreCaseAndSubjectContainingIgnoreCaseAndSchoolContainingIgnoreCase(
                        t, p, s, sch, pageable
        );

        return page.map(ProblemPostResponse::new);
    }

    //게시글 삭제하기
    @Override
    @Transactional
    public void delete(Long postId, String username) {
        ProblemPost post = problemPostRepository.findById(postId).orElseThrow(PostException.NOT_FOUND_EXCEPTION::getTaskException);
        if(post.getMember().getUsername() != username) {
            throw PostException.NOT_MATCHED_AUTHOR_EXCEPTION.getTaskException();
        }

        problemPostRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public ProblemPostResponse update(Long postId, ProblemPostRequest request, MultipartFile problemData, String username) {
        // 1) 게시글 조회
        ProblemPost post = problemPostRepository.findById(postId)
                .orElseThrow(PostException.NOT_FOUND_EXCEPTION::getTaskException);

        // 2) 작성자 검증
        if (!post.getMember().getUsername().equals(username)) {
            throw PostException.NOT_MATCHED_AUTHOR_EXCEPTION.getTaskException();
        }

        // 3) 파일 업로드 (새 파일이 있을 때만)
        if (problemData != null && !problemData.isEmpty()) {
            String newFilename = problemPostDataRepository.upload(problemData);
            request.setProblemData(newFilename);
        }

        // 4) 엔티티 업데이트 (Dirty Checking)
        post.update(
                request.getTitle(),
                request.getContent(),
                request.getProfessorName(),
                request.getSchool(),
                request.getSubject(),
                request.getProblemData()
        );

        // 5) 응답 생성
        return new ProblemPostResponse(post);
    }
}
