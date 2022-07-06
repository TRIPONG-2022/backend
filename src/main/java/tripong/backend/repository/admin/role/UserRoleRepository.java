package tripong.backend.repository.admin.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripong.backend.entity.role.UserRole;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {


}
