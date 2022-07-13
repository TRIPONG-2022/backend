package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.authorization.CustomFilterInvocationSecurityMetadataSource;
import tripong.backend.config.security.authorization.MethodResourceLiveUpdateService;
import tripong.backend.dto.admin.resource.CreateResourceFormRequestDto;
import tripong.backend.dto.admin.resource.CreateResourceRequestDto;
import tripong.backend.dto.admin.resource.DeleteResourceReloadDto;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.entity.role.ResourceType;
import tripong.backend.service.admin.ResourceService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminResourceApiController {

    private final ResourceService resourceService;
    private final CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    private final MethodResourceLiveUpdateService methodResourceLiveUpdateService;

    /**
     * 자원 전체 목록 API
     */
    @GetMapping("/admin/resources")
    public ResponseEntity getResourceList(@PageableDefault(sort = "priorityNum", direction = Sort.Direction.DESC) Pageable pageable){
        Page<GetResourceListResponseDto> result = resourceService.getResourceList(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 자원 생성 폼 요소들 반환 API
     */
    @GetMapping("/admin/resources/form")
    public ResponseEntity createResourceForm(){
        CreateResourceFormRequestDto result = resourceService.createResourceForm();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 자원 등록 API
     */
    @PostMapping("/admin/resources")
    public ResponseEntity createResource(@RequestBody CreateResourceRequestDto dto) throws Exception {
        resourceService.createResource(dto);
        if(dto.getResourceType()== ResourceType.Url) {
            customFilterInvocationSecurityMetadataSource.reload_url();
        } else{
            methodResourceLiveUpdateService.reload_add_method(dto.getResourceName(), dto.getRoleNames());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 자원 삭제 API
     */
    @DeleteMapping("/admin/resources/{resourceId}")
    public ResponseEntity deleteResource(@PathVariable("resourceId") Long resourceId) throws Exception {
        DeleteResourceReloadDto dto = resourceService.deleteResource(resourceId);
        if(dto.getResourceType()==ResourceType.Url){
            customFilterInvocationSecurityMetadataSource.reload_url();
        } else{
            methodResourceLiveUpdateService.reload_delete_method(dto.getResourceName());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
