package tripong.backend.service.report;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.report.PostReportRequestDto;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.report.PostReport;
import tripong.backend.entity.report.UserReport;
import tripong.backend.entity.user.User;
import tripong.backend.exception.report.ReportErrorMessage;
import tripong.backend.repository.post.PostRepository;
import tripong.backend.repository.report.PostReportRepository;
import tripong.backend.repository.report.UserReportRepository;
import tripong.backend.repository.user.UserRepository;

import java.util.NoSuchElementException;

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
    public void userReport(UserReportRequestDto dto, Long reportedId, AuthDetail principal) {
        log.info("시작: ReportService 유저 리포트");
        User reported_user = userRepository.findById(reportedId).orElseThrow(()->new NoSuchElementException("신고 받을 유저가 없습니다. reportedId=" + reportedId));
        if(reported_user.getId() == principal.getPk()){
            throw new IllegalStateException(ReportErrorMessage.MySelf_USER_IMPOSSIBLE);
        }
        User report_user = userRepository.findById(principal.getPk()).orElseThrow(() -> new NoSuchElementException("신고를 행하는 유저가 없습니다. reportId=" + principal.getPk()));
        userReportRepository.save(new UserReport(reported_user, report_user, dto.getKind()));
        log.info("종료: ReportService 유저 리포트");
    }

    /**
     * 게시물 신고
     * -존재하지 않는 postId 신고시, 에러처리
     * -자신의 게시글 신고시, 에러처리
     */
    @Transactional
    public void postReport(PostReportRequestDto dto, Long reportedPostId, AuthDetail principal) {
        log.info("시작: ReportService 게시물 리포트");
        Post reported_post = postRepository.findById(reportedPostId).orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. reportedPostId=" + reportedPostId));
        if(reported_post.getAuthor().getId() == principal.getPk()){
            throw new IllegalStateException(ReportErrorMessage.MySelf_POST_IMPOSSIBLE);
        }
        User report_user = userRepository.findById(principal.getPk()).orElseThrow(() -> new NoSuchElementException("신고를 행하는 유저가 없습니다. reportId=" + principal.getPk()));
        postReportRepository.save(new PostReport(reported_post, report_user, dto.getKind()));
        log.info("종료: ReportService 게시물 리포트");
    }
}
