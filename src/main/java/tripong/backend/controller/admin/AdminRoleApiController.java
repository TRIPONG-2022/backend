package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.dto.admin.role.CreateRoleRequestDto;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.admin.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminRoleApiController {

    private final RoleService roleService;

    /**
     * 권한 전체 목록 API
     */
    @GetMapping("/admin/roles")
    public ResponseEntity getRoleList(){
        List<GetRoleListResponseDto> getRoleListResponseDtoList = roleService.getRoleList();
        return new ResponseEntity<>(getRoleListResponseDtoList, HttpStatus.OK);
    }

    /**
     * 권한 등록 API
     */
    @PostMapping("/admin/roles")
    public ResponseEntity createRole(@Validated @RequestBody CreateRoleRequestDto dto, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        roleService.createRole(dto);
        response.setHeader("Location", "/admin/roles");
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }

    /**
     * 권한 삭제 API
     */
    @DeleteMapping("/admin/roles/{roleId}")
    public ResponseEntity deleteRole(@PathVariable("roleId") Long roleId, HttpServletResponse response){
        roleService.deleteRole(roleId);
        response.setHeader("Location", "/admin/roles");
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }
}
