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
import tripong.backend.dto.admin.user.GetUserAllListDto;
import tripong.backend.dto.admin.user.GetUserReportedListResponseDto;
import tripong.backend.dto.admin.user.UpdateRolesRequestDto;
import tripong.backend.service.admin.AdminService;

/**
 * AdminUserController: 유저 & 게시글 관리
 * RoleDataController: 권한 데이터 관리
 * ResourceDataController: 자원 데이터 관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminUserController {

    private final AdminService adminService;

    /**
     * 유저 전체 목록 API
     */
    @GetMapping("/admin/users")
    public ResponseEntity getUserList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetUserAllListDto> userList = adminService.getUserList(pageable);
        return new ResponseEntity<>(userList, HttpStatus.OK);
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
     * 신고 접수(유저) 전체 목록 API
     */
    @GetMapping("/admin/reports/users")
    public ResponseEntity getUserReportedList(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetUserReportedListResponseDto> result = adminService.getUserReportedList(pageable);
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
