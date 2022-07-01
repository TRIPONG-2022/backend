package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.admin.resource.CreateResourceRequestDto;
import tripong.backend.dto.admin.resource.DeleteResourceRequestDto;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.role.RoleResource;
import tripong.backend.repository.admin.resource.ResourceRepository;
import tripong.backend.repository.admin.role.RoleRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;

    /**
     * DB 자원 requestMap 변환
     */
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getUrlRequestMap(){
        List<Resource> resources = resourceRepository.findUrlAllResources();
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> beforeRequestMap = new LinkedHashMap<>();
        resources.forEach( r->{
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            r.getRoleResources().forEach( role ->{
                configAttributeList.add(new SecurityConfig(role.getRole().getRoleName()));
            });
            beforeRequestMap.put(new AntPathRequestMatcher(r.getResourceName()), configAttributeList);
        });
        return beforeRequestMap;
    }


    /**
     * 자원 전체 목록
     */
    public Page<GetResourceListResponseDto> getResourceList(Pageable pageable) {
        log.info("시작: ResourceService 자원리스트");

        Page<Resource> page = resourceRepository.findAllWithRoles(pageable);
        List<GetResourceListResponseDto> result = page.stream()
                        .map(r -> new GetResourceListResponseDto(r))
                                .collect(Collectors.toList());

        log.info("종료: ResourceService 자원리스트");
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    /**
     * 자원 등록
     * -이미 존재하는 자원이면 에러 처리
     */
    @Transactional
    public void createResource(CreateResourceRequestDto dto) {
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
    public void deleteResource(DeleteResourceRequestDto dto) {
        log.info("종료: ResourceService 자원삭제");

        Resource resource = resourceRepository.findByResourceName(dto.getResourceName());
        resourceRepository.delete(resource);

        log.info("종료: ResourceService 자원삭제");
    }


}










