package tripong.backend.controller.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;
import tripong.backend.service.post.PostService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostApiController {

    private final PostService postService;

    @GetMapping("/{category}")
    public ResponseEntity<List<PostResponseDto>> getListGathering(@PathVariable Category category, Pageable pageable) {
        log.info("postType = {}", category);
        log.info("page = {}", pageable.getPageNumber());
        log.info("size = {}", pageable.getPageSize());
        log.info("sort = {}", pageable.getSort());
        List<PostResponseDto> postResponseDtoList = postService.findByCategory(category, pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/{category}/{postId}")
    public ResponseEntity<PostResponseDto> getGathering(@PathVariable Long postId) {
        log.info("get postId = {}", postId);
        PostResponseDto postResponseDto = postService.findById(postId);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{category}")
    public ResponseEntity<Object> saveGathering(@ModelAttribute PostRequestDto postRequestDto) {
        Post post = postService.save(postRequestDto);
        log.info("saved postId = {}", post.getId());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/{category}/{postId}")
    public ResponseEntity<Object> saveGathering(@PathVariable Long postId, @ModelAttribute PostRequestDto postRequestDto) {
        log.info("update postId = {}", postId);
        postService.update(postId, postRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{category}/{postId}")
    public ResponseEntity<Object> deleteGathering(@PathVariable Long postId) {
        log.info("delete postId = {}", postId);
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
