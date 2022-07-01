package tripong.backend.repository.admin.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.entity.role.Resource;

import java.util.List;


public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Resource findByResourceNameAndMethodName(String resourceName, String methodName);

    @Query("select r from Resource r join fetch r.roleResources where r.resourceType = 'Url' order by r.priorityNum desc") //
    List<Resource> findUrlAllResources();
}
