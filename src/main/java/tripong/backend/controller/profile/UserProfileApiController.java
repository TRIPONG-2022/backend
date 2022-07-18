package tripong.backend.controller.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.post.PostResponseDto;
import tripong.backend.dto.reply.ReplyResponseDto;
import tripong.backend.dto.profile.UserProfileRequestDto;
import tripong.backend.dto.profile.UserProfileResponseDto;
import tripong.backend.entity.post.Category;
import tripong.backend.service.post.PostService;
import tripong.backend.service.profile.UserProfileService;
import tripong.backend.service.reply.ReplyService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/profile")
public class UserProfileApiController {

    private final PostService postService;

    private final ReplyService replyService;

    private final UserProfileService userProfileService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getListPosts(@RequestParam Category category, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, Pageable pageable, @AuthenticationPrincipal PrincipalDetail principal) {
        List<PostResponseDto> postResponseDtoList = postService.getPersonalPostList(principal.getUser().getId(), category, fromDate, endDate, pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/likes")
    public ResponseEntity<List<PostResponseDto>> getListPosts(@RequestParam Category category, Pageable pageable, @AuthenticationPrincipal PrincipalDetail principal) {
        List<PostResponseDto> postResponseDtoList = postService.getPersonalLikePostList(principal.getUser().getId(), category, pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

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

    @GetMapping("replies/{userId}")
    public ResponseEntity<List<ReplyResponseDto>> getReplyListByUserId(@PathVariable String userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, Pageable pageable) {

        LocalDateTime startDate = fromDate.atStartOfDay();
        LocalDateTime finishDate = endDate.atStartOfDay();

        List<ReplyResponseDto> replyList = replyService.getReplyListByUserId(userId, startDate, finishDate, pageable);
        return new ResponseEntity<>(replyList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        UserProfileResponseDto userProfileResponseDto = userProfileService.getUserProfile(userId);
        return new ResponseEntity<>(userProfileResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@AuthenticationPrincipal PrincipalDetail principal) {
        UserProfileResponseDto userProfileResponseDto = userProfileService.getUserProfile(principal.getUser().getId());
        return new ResponseEntity<>(userProfileResponseDto, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Object> updateUserProfile(@PathVariable Long userId, @ModelAttribute UserProfileRequestDto userProfileRequestDto, @AuthenticationPrincipal PrincipalDetail principal) {
        userProfileService.updateUserProfile(principal.getUser().getId(), userProfileRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
