package tripong.backend.controller.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.reply.ReplyRequestDto;
import tripong.backend.dto.reply.ReplyResponseDto;
import tripong.backend.entity.reply.Reply;
import tripong.backend.service.reply.ReplyService;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyRestController {

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
    public ResponseEntity<Object> saveReply(@RequestBody ReplyRequestDto dto){
        replyService.saveReply(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 및 대댓글 수정
    @PatchMapping("/replies/{id}")
    public ResponseEntity<Object> updateReply(@PathVariable Long id, @RequestBody ReplyRequestDto dto){
        dto.setId(id);
        replyService.updateReply(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 및 대댓글 삭제
    @DeleteMapping("/replies/{id}")
    public ResponseEntity<Object> deleteReply(@PathVariable Long id){
        replyService.deleteReply(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}