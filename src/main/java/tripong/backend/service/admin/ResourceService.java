package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.auth.authorization.CustomFilterInvocationSecurityMetadataSource;
import tripong.backend.config.auth.authorization.MethodResourceLiveUpdateService;
import tripong.backend.dto.admin.resource.CreateResourceFormRequestDto;
import tripong.backend.dto.admin.resource.CreateResourceRequestDto;
import tripong.backend.dto.admin.resource.DeleteResourceReloadDto;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.role.RoleResource;
import tripong.backend.repository.admin.resource.ResourceRepository;
import tripong.backend.repository.admin.role.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * 자원 등록 폼
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
    public void createResource(CreateResourceRequestDto dto) throws Exception {
        log.info("시작: ResourceService 자원등록");

        Resource resource = resourceRepository.findByResourceName(dto.getResourceName());
        if(resource!=null){
            throw new IllegalStateException("이미 존재하는 자원입니다. 삭제 후 등록해 주세요");
        }

        List<RoleResource> roleResources = new ArrayList<>();
        for(String role_name : dto.getRoleNames()){
            Role r = roleRepository.findByRoleName(role_name);
            RoleResource roleResource = RoleResource.builder().role(r).build();
            roleResources.add(roleResource);
        }
        Resource resource_build = Resource.builder()
                .resourceName(dto.getResourceName())
                .resourceType(dto.getResourceType())
                .description(dto.getDescription())
                .roleResources(roleResources)
                .priorityNum(dto.getPriorityNum())
                .build();
        resourceRepository.save(resource_build);

        log.info("종료: ResourceService 자원등록");
    }


    /**
     * 자원 삭제
     * -자원 전체 목록에서 보이는 것을 삭제하므로, DB 에러 생략
     */
    @Transactional
    public DeleteResourceReloadDto deleteResource(Long resourceId) throws Exception {
        log.info("시작: ResourceService 자원삭제");

        Optional<Resource> resource = resourceRepository.findById(resourceId);
        if(resource.isPresent()){
            resourceRepository.delete(resource.get());
            log.info("종료: ResourceService 자원삭제");

            return new DeleteResourceReloadDto(resource.get().getResourceType(), resource.get().getResourceName());
        }
        else{
            throw new IllegalStateException("존재하지 않는 자원입니다.");
        }
    }





}










