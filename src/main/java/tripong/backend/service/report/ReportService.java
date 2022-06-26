package tripong.backend.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.dto.report.PostReportRequestDto;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.user.User;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.report.PostReportRepository;
import tripong.backend.repository.report.UserReportRepository;
import tripong.backend.repository.user.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;


    /**
     * 유저 신고
     * -존재하지 않는 Id는 신고시, 에러처리
     * -자신 신고시, 에러처리
     */
    @Transactional
    public void userReport(UserReportRequestDto dto, Long reportedId, PrincipalDetail principal) {
        log.info("시작: ReportService 유저 리포트");

        Optional<User> reported_user = userRepository.findById(reportedId);

        if(!reported_user.isPresent()){
            throw new NullPointerException("존재하지 않는 reportedId 사용자");
        }
        if(reported_user.get().getId() == principal.getUser().getId()){
            throw new IllegalStateException("자기 자신은 신고 불가");
        }

        dto.setReportUserId(principal.getUser());
        dto.setReportedUserId(reported_user.get());
        userReportRepository.save(dto.toEntity());
        log.info("종료: ReportService 유저 리포트");
    }

    /**
     * 게시물 신고
     * -존재하지 않는 postId 신고시, 에러처리
     * -자신의 게시글 신고시, 에러처리
     */
    @Transactional
    public void postReport(PostReportRequestDto dto, Long reportedPostId, PrincipalDetail principal) {
        log.info("시작: ReportService 게시물 리포트");

        Optional<Post> reported_post = postRepository.findById(reportedPostId);
        if(!reported_post.isPresent()){
            throw new NullPointerException("존재하지 않는 postId를 신고");
        }
        if(reported_post.get().getUser().getId() == principal.getUser().getId()){
            throw new IllegalStateException("자기 자신의 게시글 신고 불가");
        }

        dto.setReportedPostId(reported_post.get());
        dto.setReportUserId(principal.getUser());
        postReportRepository.save(dto.toEntity());

        log.info("종료: ReportService 게시물 리포트");
    }
}
