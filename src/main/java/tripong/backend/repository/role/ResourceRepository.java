package tripong.backend.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.role.Resource;


public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Resource findByResourceNameAndMethodName(String resourceName, String methodName);

}
