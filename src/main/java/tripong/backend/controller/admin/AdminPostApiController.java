package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.admin.post.GetPostAllListDto;
import tripong.backend.dto.admin.post.GetPostReportedListResponseDto;
import tripong.backend.dto.admin.user.GetUserAllListDto;
import tripong.backend.service.admin.AdminService;
import tripong.backend.service.post.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminPostApiController {

    private final AdminService adminService;
    private final PostService postService;

    /**
     * 게시글 전체 목록 API
     */
    @GetMapping("/admin/posts")
    public ResponseEntity getPostList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetPostAllListDto> result = adminService.getPostList(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 신고 접수(게시글) 전체 목록 API
     */
    @GetMapping("/admin/reports/posts")
    public ResponseEntity getPostReportedList(@PageableDefault(sort = "reportCreatedDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetPostReportedListResponseDto> result = adminService.getPostReportedList(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 신고 게시글 삭제 API
     */
    @DeleteMapping("/admin/reports/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("postId") Long postId){
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
