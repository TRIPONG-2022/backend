package tripong.backend.controller.admin;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.admin.user.GetUserAllListDto;
import tripong.backend.dto.admin.user.GetUserReportedListResponseDto;
import tripong.backend.dto.admin.user.UpdateRolesRequestDto;
import tripong.backend.service.admin.AdminService;

/**
 * AdminController: 유저 & 게시글 관리
 * RoleDataController: 권한 데이터 관리
 * ResourceDataController: 자원 데이터 관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    /**
     * 사용자 전체 목록 API
     */
    @GetMapping("/admin/users")
    public ResponseEntity getUserList(Pageable pageable){
        log.info("시작: AdminController 사용자전체목록");

        Page<GetUserAllListDto> userList = adminService.getUserList(pageable);

        log.info("종료: AdminController 사용자전체목록");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(userList, status);
    }


    /**
     * 사용자 권한 변경 API
     */
    @PatchMapping("/admin/users/{userId}")
    public ResponseEntity changedRoles(@PathVariable("userId") Long userId, @RequestBody UpdateRolesRequestDto dto){
        log.info("시작: AdminController 권한변경");

        adminService.changedRoles(userId, dto);


        log.info("종료: AdminController 권한변경");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

    /**
     * 신고 받은 사용자 전체 목록 API
     */
    @GetMapping("/admin/reports/users")
    public ResponseEntity getUserReportedList(Pageable pageable){
        log.info("시작: AdminController 신고유저전체목록");

        Page<GetUserReportedListResponseDto> result = adminService.getUserReportedList(pageable);

        log.info("종료: AdminController 신고유저전체목록");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(result, status);
    }

    /**
     * 신고 받은 게시글 전체 목록 API
     */


    /**
     * 신고 유저 정지(BLACK) 처리 API
     */
    @PatchMapping("/admin/reports/users/{userId}")
    public ResponseEntity changedBlack(@PathVariable("userId") Long userId){
        log.info("시작: AdminController 블랙처리");

        adminService.changedBlack(userId);

        log.info("종료: AdminController 블랙처리");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

}
