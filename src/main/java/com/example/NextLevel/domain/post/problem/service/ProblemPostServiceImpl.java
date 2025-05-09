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
}
