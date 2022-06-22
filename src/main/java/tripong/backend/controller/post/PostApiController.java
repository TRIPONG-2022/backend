package tripong.backend.controller.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.entity.post.Post;
import tripong.backend.service.post.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostApiController {

    private final PostService postService;

    @PostMapping("/gathering")
    public ResponseEntity<Object> saveGathering(@ModelAttribute PostRequestDto postRequestDto) {
        Post post = postService.save(postRequestDto);
        log.info("saved postId = {}", post.getId());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/gathering/{postId}")
    public ResponseEntity<Post> getGathering(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/gathering/{postId}")
    public ResponseEntity<Object> deleteGathering(@PathVariable Long postId) {
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
