package tripong.backend.controller.reply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.principal.AuthDetail;
import tripong.backend.dto.reply.ReplyRequestDto;
import tripong.backend.dto.reply.ReplyResponseDto;
import tripong.backend.entity.reply.Reply;
import tripong.backend.exception.ErrorResult;
import tripong.backend.service.reply.ReplyService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyApiController {

    private final ReplyService replyService;

    // 댓글 조회
    @GetMapping("/replies/parent/{postId}")
    public ResponseEntity<Object> getListParentReply(@PathVariable Long postId, Pageable pageable){

        List<ReplyResponseDto> replyResponseDtoList = replyService.getListParentReply(postId, pageable);

        return new ResponseEntity<>(replyResponseDtoList, HttpStatus.OK);

    }

    // 대댓글 조회
    @GetMapping("/replies/children/{postId}/{parentReply}")
    public ResponseEntity<Object> getListChildrenReply(@PathVariable Long postId, @PathVariable Long parentReply, Pageable pageable){

        List<ReplyResponseDto> replyResponseDtoList = replyService.getListChildrenReply(postId, parentReply, pageable);

        return new ResponseEntity<>(replyResponseDtoList, HttpStatus.OK);

    }

    // 댓글 및 대댓글 작성
    @PostMapping("/replies/{postId}")
    public ResponseEntity<Object> saveReply(@PathVariable Long postId, @Validated @RequestBody ReplyRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal AuthDetail principal){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Reply reply = replyService.saveReply(postId, dto, principal);

        log.info(reply.getId() + "번 댓글 작성 완료");
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    // 댓글 및 대댓글 수정
    @PatchMapping("/replies/{postId}/{id}")
    public ResponseEntity<Object> updateReply(@PathVariable Long id, @Validated @RequestBody ReplyRequestDto dto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        replyService.updateReply(id, dto);

        log.info(id + "번 댓글 업데이트 완료");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    // 댓글 및 대댓글 삭제
    @DeleteMapping("/replies/{postId}/{id}")
    public ResponseEntity<Object> deleteReply(@PathVariable Long id){

        replyService.deleteReply(id);

        log.info(id + "번 댓글 삭제 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}