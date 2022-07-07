package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.admin.post.GetPostReportedListResponseDto;
import tripong.backend.service.admin.AdminService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminPostController {

    private final AdminService adminService;

    /**
     * 신고 받은 게시글 전체 목록 API
     */
    @GetMapping("/admin/reports/posts")
    public ResponseEntity getPostReportedList(Pageable pageable){
        log.info("시작: AdminPostController 신고게시글전체목록");

        Page<GetPostReportedListResponseDto> result = adminService.getPostReportedList(pageable);

        log.info("종료: AdminPostController 신고게시글전체목록");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(result, status);
    }

    /**
     * 신고 받은 게시글 삭제 API
     */
    @DeleteMapping("/admin/reports/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("postId") Long postId){
        log.info("시작: AdminPostController 신고게시글삭제");
        adminService.deletePost(postId);
        log.info("종료: AdminPostController 신고게시글삭제");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }






}
