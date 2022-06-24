package tripong.backend.controller.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.service.report.ReportService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    //신고 카테고리 정해진 후, 설계

    /**
     * 유저 신고 API
     * -{userId}: 신고받을 유저의 pk (없는 pk 에러처리 -> 추후 설계예정)
     */
    @PostMapping("/reports/{userId}") //jwt인데 userId를?
    public ResponseEntity userReport(@RequestBody UserReportRequestDto dto, @PathVariable("userId") Long reportedId, @AuthenticationPrincipal PrincipalDetail principal){
        reportService.userReport(dto, reportedId, principal);

        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

}
