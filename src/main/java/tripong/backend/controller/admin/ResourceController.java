package tripong.backend.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.admin.resource.CreateResourceRequestDto;
import tripong.backend.dto.admin.resource.DeleteResourceRequestDto;
import tripong.backend.dto.admin.resource.GetResourceListResponseDto;
import tripong.backend.dto.admin.role.DeleteRoleRequestDto;
import tripong.backend.dto.admin.role.GetRoleListResponseDto;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.ResourceType;
import tripong.backend.repository.admin.resource.ResourceRepository;
import tripong.backend.service.admin.ResourceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ResourceController {

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
    @DeleteMapping("/admin/resources")
    public ResponseEntity deleteResource(@RequestBody DeleteResourceRequestDto dto){
        log.info("시작: ResourceController 자원삭제");

        resourceService.deleteResource(dto);

        log.info("종료: ResourceController 자원삭제");
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(status);
    }

}
