package tripong.backend.repository.admin.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.entity.role.Resource;

import java.util.List;
import java.util.Optional;


public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("select distinct r from Resource r left join fetch r.roleResources re join fetch re.role where r.resourceType = 'Url' order by r.priorityNum desc")
    List<Resource> findUrlAllResources();

    @Query("select distinct r from Resource r join fetch r.roleResources re join fetch re.role where r.resourceType = 'Method' order by r.priorityNum desc")
    List<Resource> findMethodAllResources();

    @Query(value = "select r from Resource r")
    Page<Resource> findPagingAll(Pageable pageable);

    Optional<Resource> findByResourceName(String resourceName);


    @Query(value = "select r from Resource r order by r.priorityNum desc")
    List<Resource> findReason();
}
