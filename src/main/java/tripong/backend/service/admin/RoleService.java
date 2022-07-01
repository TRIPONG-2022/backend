package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.role.CreateRoleRequestDto;
import tripong.backend.dto.admin.role.DeleteRoleRequestDto;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.entity.role.Role;
import tripong.backend.repository.admin.role.RoleRepository;

import java.util.List;

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
            throw new IllegalArgumentException("권한명 ROLE_~~ 으로 작성해 주세요.");
        }

        Role role = Role.builder()
                .roleName(dto.getRoleName())
                .description(dto.getDescription())
                .build();

        roleRepository.save(role);
    }

    /**
     *  권한 삭제
     * */
    @Transactional
    public void deleteRole(DeleteRoleRequestDto dto) {
        Role role = roleRepository.findByRoleName(dto.getRoleName());
        roleRepository.delete(role);
    }


}
