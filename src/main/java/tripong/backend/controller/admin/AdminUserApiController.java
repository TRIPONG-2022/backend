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
import tripong.backend.dto.admin.user.GetUserPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.GetUserReportedPageANDSearchResponseDto;
import tripong.backend.dto.admin.user.UpdateRolesRequestDto;
import tripong.backend.dto.search.SearchAdminUserType;
import tripong.backend.service.admin.AdminService;

/**
 * AdminUserApiController: 유저 & 게시글 관리
 * AdminRoleApiController: 권한 데이터 관리
 * AdminResourceApiController: 자원 데이터 관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminUserApiController {

    private final AdminService adminService;

    /**
     * 유저 전체 목록 + 검색 API
     */
    @GetMapping("/admin/users")
    public ResponseEntity getUserPageANDSearch(@RequestParam(value = "searchType", required = false) SearchAdminUserType searchType, @RequestParam(value = "keyword", required = false) String keyword, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetUserPageANDSearchResponseDto> result = adminService.getUserPageANDSearch(searchType, keyword, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 유저 권한 변경 API
     */
    @PatchMapping("/admin/users/{userId}")
    public ResponseEntity changedRoles(@PathVariable("userId") Long userId, @RequestBody UpdateRolesRequestDto dto){
        adminService.changedRoles(userId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 신고 접수(유저) 전체 목록 + 검색 API
     */
    @GetMapping("/admin/reports/users")
    public ResponseEntity getUserReportedPageANDSearch(@RequestParam(value = "searchType", required = false) SearchAdminUserType searchType, @RequestParam(value = "keyword", required = false) String keyword, @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetUserReportedPageANDSearchResponseDto> result = adminService.getUserReportedPageANDSearch(searchType, keyword, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    /**
     * 유저 BLACK 처리 API
     */
    @PatchMapping("/admin/reports/users/black/{userId}")
    public ResponseEntity changedBlack(@PathVariable("userId") Long userId){
        adminService.changedBlack(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
