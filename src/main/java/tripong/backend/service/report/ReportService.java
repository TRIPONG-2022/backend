package tripong.backend.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.entity.user.User;
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


    /**
     * 유저신고
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

        dto.setReportId(principal.getUser());
        dto.setReportedId(reported_user.get());
        userReportRepository.save(dto.toEntity());
        log.info("종료: ReportService 유저 리포트");
    }

}
