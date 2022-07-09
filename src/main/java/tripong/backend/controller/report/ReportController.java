package tripong.backend.controller.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.auth.PrincipalDetail;
import tripong.backend.dto.report.PostReportRequestDto;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.entity.report.ReportType;
import tripong.backend.service.report.ReportService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;


    /**
    * 유저 신고 카테고리 API
    */
    @GetMapping("/reports/type")
    public ResponseEntity userReportType(){
        List<ReportType> types = Arrays.asList(ReportType.values());

        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(types, status);
    }

    /**
     * 유저 신고 등록 API
     * -{userId}: 신고받을 유저의 pk (없는 pk 에러처리 -> 추후 설계예정)
     */
    @PostMapping("/reports/users/{userId}")
    public ResponseEntity userReport(@RequestBody UserReportRequestDto dto, @PathVariable("userId") Long reportedUserId, @AuthenticationPrincipal PrincipalDetail principal){
        reportService.userReport(dto, reportedUserId, principal);

        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }


    /**
     * 게시글 신고 등록 API
     * -{postId}: 신고받을 게시글의 pk
     */
    @PostMapping("/reports/posts/{postId}")
    public ResponseEntity postReport(@RequestBody PostReportRequestDto dto, @PathVariable("postId") Long reportedPostId, @AuthenticationPrincipal PrincipalDetail principal){
        reportService.postReport(dto, reportedPostId, principal);

        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

}
