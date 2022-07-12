package tripong.backend.dto.reply;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.reply.Reply;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyProfileResponseDto {

    private Long id;

    private Long postId;

    private String userId;

    private String content;

    private Long parentReply;

    @Builder
    public ReplyProfileResponseDto(Reply reply) {
        this.id = reply.getId();
        this.postId = reply.getPostId();
        this.userId = reply.getUserId().getLoginId();
        this.content = reply.getContent();
        this.parentReply = reply.getParentReply().getId();
    }
}
