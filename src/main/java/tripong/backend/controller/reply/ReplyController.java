package tripong.backend.controller.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.dto.reply.ReplyRequestDto;
import tripong.backend.service.reply.ReplyService;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 댓글 및 대댓글 작성
    @PostMapping("/replies/{postId}")
    public ResponseEntity<Object> saveReply(@RequestBody ReplyRequestDto dto){
        replyService.saveReply(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}