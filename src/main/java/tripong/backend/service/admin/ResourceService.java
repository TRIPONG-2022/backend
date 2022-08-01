package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.authorization.CustomFilterInvocationSecurityMetadataSource;
import tripong.backend.config.security.authorization.MethodResourceLiveUpdateService;
import tripong.backend.dto.admin.resource.*;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.role.RoleResource;
import tripong.backend.exception.admin.AdminErrorMessage;
import tripong.backend.controller.report.admin.resource.ResourceRepository;
import tripong.backend.controller.report.admin.role.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;
    private final CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    private final MethodResourceLiveUpdateService methodResourceLiveUpdateService;


    /**
     * 자원 전체 목록
     */
    public Page<GetResourceListResponseDto> getResourceList(Pageable pageable) {
        log.info("시작: ResourceService 자원리스트");
        Page<Resource> page = resourceRepository.findPagingAll(pageable);
        log.info("종료: ResourceService 자원리스트");
        return page.map(p -> new GetResourceListResponseDto(p));
    }

    /**
     * 자원 생성 폼 요소들
     * -권한의 종류와, 적용 가능한 자원 방식을 반환
     */
    public CreateResourceFormRequestDto createResourceForm() {
        return new CreateResourceFormRequestDto(roleRepository.findRoleNamesAll());
    }

    /**
     * 자원 등록
     * -이미 존재하는 자원이면 에러 처리
     */
    @Transactional
    public void createResource(CreateResourceRequestDto dto) {
        log.info("시작: ResourceService 자원등록");
        if(resourceRepository.findByResourceName(dto.getResourceName()).isPresent()){
            throw new IllegalStateException(AdminErrorMessage.ResourceName_DUP);
        }
        List<RoleResource> roleResources = new ArrayList<>();
        for(String role_name : dto.getRoles()){
            Role role = roleRepository.findByRoleName(role_name).get();
            roleResources.add(new RoleResource(role));
        }
        resourceRepository.save(new Resource(dto.getResourceName(), dto.getResourceType(), dto.getDescription(), dto.getPriorityNum(), roleResources));
        log.info("종료: ResourceService 자원등록");
    }


    /**
     * 자원 삭제
     * -자원 전체 목록에서 보이는 것을 삭제하므로, DB 에러 생략
     */
    @Transactional
    public DeleteResourceReloadDto deleteResource(Long resourceId) throws Exception {
        log.info("시작: ResourceService 자원삭제");
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new NoSuchElementException("해당 자원이 없습니다. resourceId=" + resourceId));
        resourceRepository.delete(resource);
        log.info("종료: ResourceService 자원삭제");
        return new DeleteResourceReloadDto(resource.getResourceType(), resource.getResourceName());
    }


    /**
     * 접근 제한: 권한명 필터용
     */
    public List<AccessDeniedReasonDto> findAccessReason(){
        List<AccessDeniedReasonDto> result = new ArrayList<>();
        resourceRepository.findReason().stream().forEach(r -> result.add(new AccessDeniedReasonDto(r)));
        return result;
    }
}










