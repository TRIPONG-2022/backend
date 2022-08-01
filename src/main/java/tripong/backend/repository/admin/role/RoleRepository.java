package tripong.backend.repository.admin.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.entity.role.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByRoleName(String roleName);

    @Query("select new tripong.backend.dto.admin.role.GetRoleListResponseDto(r.id, r.roleName, r.description) from Role r")
    List<GetRoleListResponseDto> findGetByAll();

    @Query("select r.roleName from Role r")
    List<String> findRoleNamesAll();


}
