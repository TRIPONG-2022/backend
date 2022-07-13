package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.role.CreateRoleRequestDto;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.entity.role.Role;
import tripong.backend.exception.admin.AdminErrorMessage;
import tripong.backend.repository.admin.role.RoleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * 권한 전체 목록
     */
    public List<GetRoleListResponseDto> getRoleList() {
        return roleRepository.findGetByAll();
    }

    /**
     *  권한 등록
     *  - ROLE_ 으로 시작안하는 경우 에러 반환
     * */
    @Transactional
    public void createRole(CreateRoleRequestDto dto) {
        log.info("시작: RoleService 권한등록");
        if(!dto.getRoleName().startsWith("ROLE_")){
            throw new IllegalArgumentException(AdminErrorMessage.Role_FORM_ERROR);
        }
        roleRepository.save(new Role(dto.getRoleName(), dto.getDescription()));
        log.info("종료: RoleService 권한등록");
    }

    /**
     *  권한 삭제
     * */
    @Transactional
    public void deleteRole(Long roleId) {
        log.info("시작: RoleService 권한삭제");
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new NoSuchElementException("해당 권한이 없습니다. roleId=" + roleId));
        roleRepository.delete(role);
        log.info("종료: RoleService 권한삭제");
    }
}
