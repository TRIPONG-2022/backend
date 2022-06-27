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
import tripong.backend.dto.report.PostReportRequestDto;
import tripong.backend.dto.report.UserReportRequestDto;
import tripong.backend.service.report.ReportService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
    @PostMapping("/reports/users/{userId}")
    public ResponseEntity userReport(HttpServletRequest request, @RequestBody UserReportRequestDto dto, @PathVariable("userId") Long reportedUserId, @AuthenticationPrincipal PrincipalDetail principal){
        reportService.userReport(dto, reportedUserId, principal);
        Cookie[] cookies = request.getCookies();
        for(Cookie c: cookies){
            System.out.println("c = " + c.getName());
        }
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

    /**
     * 게시글 신고 API
     * -{postId}: 신고받을 게시글의 pk
     */
    @PostMapping("/reports/posts/{postId}")
    public ResponseEntity postReport(@RequestBody PostReportRequestDto dto, @PathVariable("postId") Long reportedPostId, @AuthenticationPrincipal PrincipalDetail principal){
        reportService.postReport(dto, reportedPostId, principal);

        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }


}
