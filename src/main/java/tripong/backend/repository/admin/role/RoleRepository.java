package tripong.backend.repository.admin.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.entity.role.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Role findByRoleName(String roleName);

    @Query("select new tripong.backend.dto.admin.role.GetRoleListResponseDto(r.roleName, r.description) from Role r")
    List<GetRoleListResponseDto> findGetByAll();
}
