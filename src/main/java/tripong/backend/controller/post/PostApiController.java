package tripong.backend.controller.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.post.PostService;
import tripong.backend.service.redis.RedisService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostApiController {

    private final PostService postService;

    private final RedisService redisService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getListAllPosts(Pageable pageable) {
        List<PostResponseDto> postResponseDtoList = postService.findAll(pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<PostResponseDto>> getListPosts(@PathVariable Category category, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList = postService.findByCategory(category, pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/{category}/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.findById(postId);
        log.info("현재 {}번 게시물의 조회수는 = {}", postId, redisService.incVisitCount(postId));
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{category}")
    public ResponseEntity<Object> savePost(@ModelAttribute @Valid PostRequestDto postRequestDto, BindingResult bindingResult, @AuthenticationPrincipal AuthDetail principal) {
        postRequestDto.setAuthor(principal.getPk());
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }
        Post post = postService.save(postRequestDto);
        log.info("saved postId = {}", post.getId());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/{category}/{postId}")
    public ResponseEntity<Object> updatePost(@PathVariable Long postId, @ModelAttribute PostRequestDto postRequestDto) {
        postService.update(postId, postRequestDto);
        log.info("updated postId = {}", postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{category}/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        log.info("deleted postId = {}", postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<Object> saveLike(@PathVariable Long postId, @AuthenticationPrincipal AuthDetail principal) {
        postService.saveLike(postId, principal.getPk());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/like/{postId}")
    public ResponseEntity<Object> deleteLike(@PathVariable Long postId, @AuthenticationPrincipal AuthDetail principal) {
        postService.deleteLike(postId, principal.getPk());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/join/{postId}")
    public ResponseEntity<Object> joinGathering(@PathVariable Long postId, @AuthenticationPrincipal AuthDetail principal) {
        postService.saveGatheringUser(postId, principal.getPk());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/join/{postId}/{userId}")
    public ResponseEntity<Object> leaveGathering(@PathVariable Long postId, @AuthenticationPrincipal AuthDetail principal) {
        postService.deleteGatheringUser(postId, principal.getPk());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/files")
    public ResponseEntity<String> putS3Image(@RequestParam MultipartFile file) {
        String fileName = postService.putS3Image(file);
        return new ResponseEntity<>(fileName, HttpStatus.CREATED);
    }

}
