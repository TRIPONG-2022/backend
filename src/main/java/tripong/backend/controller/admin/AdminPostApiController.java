package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.admin.post.GetPostPageANDSearchResponseDto;
import tripong.backend.dto.admin.post.GetPostReportedPageANDSearchResponseDto;
import tripong.backend.dto.search.SearchAdminPostType;
import tripong.backend.service.admin.AdminService;
import tripong.backend.service.post.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminPostApiController {

    private final AdminService adminService;
    private final PostService postService;

    /**
     * 게시글 전체 목록 + 검색 API
     */
    @GetMapping("/admin/posts")
    public ResponseEntity getPostPageANDSearch(@RequestParam(value = "searchType", required = false) SearchAdminPostType searchType, @RequestParam(value = "keyword", required = false) String keyword, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetPostPageANDSearchResponseDto> result = adminService.getPostPageANDSearch(searchType, keyword, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 신고 접수(게시글) 전체 목록 + 검색 API
     */
    @GetMapping("/admin/reports/posts")
    public ResponseEntity getPostReportedList(@RequestParam(value = "searchType", required = false) SearchAdminPostType searchType, @RequestParam(value = "keyword", required = false) String keyword, @PageableDefault(sort = "reportCreatedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GetPostReportedPageANDSearchResponseDto> result = adminService.getPostReportedPageANDSearch(searchType, keyword, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 신고 게시글 삭제 API
     * 창화님 postService.delete(postId) 이용
     */
    @DeleteMapping("/admin/reports/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("postId") Long postId){
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
