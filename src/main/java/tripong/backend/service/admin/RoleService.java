package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.role.CreateRoleRequestDto;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.entity.role.Role;
import tripong.backend.exception.admin.AdminErrorName;
import tripong.backend.repository.admin.role.RoleRepository;

import java.util.List;
import java.util.Optional;

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
        if(!dto.getRoleName().startsWith("ROLE_")){
            throw new IllegalArgumentException(AdminErrorName.Role_FORM_ERROR);
        }

        roleRepository.save(new Role(dto.getRoleName(), dto.getDescription()));
    }

    /**
     *  권한 삭제
     * */
    @Transactional
    public void deleteRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent()){
            roleRepository.delete(role.get());
        }
        else{
            throw new IllegalStateException(AdminErrorName.PK_NOT_ROLE);
        }
    }
}
