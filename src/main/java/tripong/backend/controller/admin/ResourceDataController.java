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
import tripong.backend.config.auth.authorization.CustomFilterInvocationSecurityMetadataSource;
import tripong.backend.config.auth.authorization.MethodResourceLiveUpdateService;
import tripong.backend.dto.admin.resource.CreateResourceFormRequestDto;
import tripong.backend.dto.admin.resource.CreateResourceRequestDto;
import tripong.backend.dto.admin.resource.DeleteResourceReloadDto;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.entity.role.ResourceType;
import tripong.backend.service.admin.ResourceService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ResourceDataController {

    private final ResourceService resourceService;
    private final CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    private final MethodResourceLiveUpdateService methodResourceLiveUpdateService;

    /**
     * 자원 전체 목록 API
     */
    @GetMapping("/admin/resources")
    public ResponseEntity getResourceList(@PageableDefault(sort = "priorityNum", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("시작: ResourceController 자원리스트");
        Page<GetResourceListResponseDto> result = resourceService.getResourceList(pageable);


        log.info("종료: ResourceController 자원리스트");
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(result, status);
    }

    /**
     * 자원 등록 폼 반환 API
     */
    @GetMapping("/admin/resources/form")
    public ResponseEntity createResourceForm(){
        log.info("시작: ResourceController 자원등록폼");

        CreateResourceFormRequestDto result = resourceService.createResourceForm();
        log.info("종료: ResourceController 자원등록폼");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(result, status);
    }

    /**
     * 자원 등록 API
     */
    @PostMapping("/admin/resources")
    public ResponseEntity createResource(@RequestBody CreateResourceRequestDto dto) throws Exception {
        log.info("시작: ResourceController 자원등록");

        resourceService.createResource(dto);

        if(dto.getResourceType()== ResourceType.Url) {
            customFilterInvocationSecurityMetadataSource.reload_url();
        } else{
            methodResourceLiveUpdateService.reload_add_method(dto.getResourceName(), dto.getRoleNames());
        }

        log.info("종료: ResourceController 자원등록");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }


    /**
     * 자원 삭제 API
     */
    @DeleteMapping("/admin/resources/{resourceId}")
    public ResponseEntity deleteResource(@PathVariable("resourceId") Long resourceId) throws Exception {
        log.info("시작: ResourceController 자원삭제");

        DeleteResourceReloadDto dto = resourceService.deleteResource(resourceId);
        if(dto.getResourceType()==ResourceType.Url){
            customFilterInvocationSecurityMetadataSource.reload_url();
        }
        else{
            methodResourceLiveUpdateService.reload_delete_method(dto.getResourceName());
        }


        log.info("종료: ResourceController 자원삭제");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

}
