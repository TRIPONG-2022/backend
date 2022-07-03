package tripong.backend.controller.admin;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.dto.admin.user.GetUserReportedListResponseDto;
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
     * 신고 받은 사용자 전체 목록 API
     */
    @GetMapping("/admin/reports/users")
    public ResponseEntity getUserReportedList(Pageable pageable){
        log.info("시작: AdminController 신고유저전체목록");

        //유저 pk, 유저이름, 유저 닉네임, 유저 아이디, 권한들 반환필요
        Page<GetUserReportedListResponseDto> result = adminService.getUserReportedList(pageable);


        log.info("종료: AdminController 신고유저전체목록");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(result, status);
    }

    /**
     * 신고 받은 게시글 전체 목록 API
     */

}
