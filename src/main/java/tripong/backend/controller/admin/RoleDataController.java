package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.admin.role.CreateRoleRequestDto;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.service.admin.RoleService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class RoleDataController {

    private final RoleService roleService;

    /**
     * 권한 전체 목록 API
     */
    @GetMapping("/admin/roles")
    public ResponseEntity getRoleList(){
        log.info("시작: RoleController 권한리스트");

        List<GetRoleListResponseDto> getRoleListResponseDtoList = roleService.getRoleList();

        log.info("종료: RoleController 권한리스트");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(getRoleListResponseDtoList, status);
    }

    /**
     * 권한 등록 API
     */
    @PostMapping("/admin/roles")
    public ResponseEntity createRole(@RequestBody CreateRoleRequestDto dto){
        log.info("시작: RoleController 권한등록");

        roleService.createRole(dto);

        log.info("종료: RoleController 권한등록");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }

    /**
     * 권한 삭제 API
     */
    @DeleteMapping("/admin/roles/{roleId}")
    public ResponseEntity deleteRole(@PathVariable("roleId") Long roleId){
        log.info("시작: RoleController 권한삭제");

        roleService.deleteRole(roleId);

        log.info("종료: RoleController 권한삭제");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }


}
