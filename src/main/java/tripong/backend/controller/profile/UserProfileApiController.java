package tripong.backend.controller.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.dto.profile.UserProfileRequestDto;
import tripong.backend.dto.profile.UserProfileResponseDto;
import tripong.backend.entity.post.Category;
import tripong.backend.service.post.PostService;
import tripong.backend.service.profile.UserProfileService;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/profile")
public class UserProfileApiController {

    private final PostService postService;

    private final UserProfileService userProfileService;

    @GetMapping("/posts/{userId}")
    public ResponseEntity<List<PostResponseDto>> getListPosts(@PathVariable Long userId, @RequestParam Category category, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList = postService.getPersonalPostList(userId, category, fromDate, endDate, pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/likes/{userId}")
    public ResponseEntity<List<PostResponseDto>> getListPosts(@PathVariable Long userId, @RequestParam Category category, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList = postService.getPersonalLikePostList(userId, category, pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        UserProfileResponseDto userProfileResponseDto = userProfileService.getUserProfile(userId);
        return new ResponseEntity<>(userProfileResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable Long userId, @ModelAttribute UserProfileRequestDto userProfileRequestDto) {
        userProfileService.updateUserProfile(userId, userProfileRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
