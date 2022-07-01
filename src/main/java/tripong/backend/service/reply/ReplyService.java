package tripong.backend.service.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.reply.ReplyRequestDto;
import tripong.backend.entity.reply.Reply;
import tripong.backend.repository.reply.ReplyRepository;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    // 댓글 및 대댓글 작성
    @Transactional
    public void saveReply(ReplyRequestDto dto){
        Reply reply = dto.toEntity();
        replyRepository.save(reply);
    }

}
