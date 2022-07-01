package tripong.backend.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.role.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Role findByRoleName(String roleName);
}
