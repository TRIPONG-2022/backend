package tripong.backend.controller.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.report.PostReportRequestDto;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.entity.report.ReportType;
import tripong.backend.service.report.ReportService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReportApiController {

    private final ReportService reportService;


    /**
    * 신고 카테고리 폼 요소 반환 API
    */
    @GetMapping("/reports/type")
    public ResponseEntity userReportType(){
        List<ReportType> types = Arrays.asList(ReportType.values());
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    /**
     * 유저 신고 등록 API
     * -{userId}: 신고받을 유저의 pk
     */
    @PostMapping("/reports/users/{userId}")
    public ResponseEntity userReport(@RequestBody UserReportRequestDto dto, @PathVariable("userId") Long reportedUserId, @AuthenticationPrincipal AuthDetail principal){
        reportService.userReport(dto, reportedUserId, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 게시글 신고 등록 API
     * -{postId}: 신고받을 게시글의 pk
     */
    @PostMapping("/reports/posts/{postId}")
    public ResponseEntity postReport(@RequestBody PostReportRequestDto dto, @PathVariable("postId") Long reportedPostId, @AuthenticationPrincipal AuthDetail principal){
        reportService.postReport(dto, reportedPostId, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
