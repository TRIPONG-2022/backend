package tripong.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.admin.resource.CreateResourceFormRequestDto;
import tripong.backend.dto.admin.resource.CreateResourceRequestDto;
import tripong.backend.dto.admin.resource.DeleteResourceRequestDto;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.service.admin.ResourceService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ResourceDataController {

    private final ResourceService resourceService;

    /**
     * 자원 전체 목록 API
     */
    @GetMapping("/admin/resources")
    public ResponseEntity getResourceList(Pageable pageable){
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
    public ResponseEntity createResource(@RequestBody CreateResourceRequestDto dto){
        log.info("시작: ResourceController 자원등록");

        resourceService.createResource(dto);

        log.info("종료: ResourceController 자원등록");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }


    /**
     * 자원 삭제 API
     */
    @DeleteMapping("/admin/resources/{resourceId}")
    public ResponseEntity deleteResource(@PathVariable("resourceId") Long resourceId){
        log.info("시작: ResourceController 자원삭제");

        resourceService.deleteResource(resourceId);

        log.info("종료: ResourceController 자원삭제");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

}
