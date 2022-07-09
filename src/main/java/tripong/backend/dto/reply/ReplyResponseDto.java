package tripong.backend.dto.reply;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.reply.Reply;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastModifiedDate;

    private Long id;

    private Long postId;

    private String userId;

    private String content;

    private Long parentReply;

    @Builder
    public ReplyResponseDto(Reply reply){
        this.createdDate = reply.getCreatedDate();
        this.lastModifiedDate = reply.getLastModifiedDate();
        this.id = reply.getId();
        this.postId = reply.getPostId();
        this.userId = reply.getUserId().getLoginId();
        this.content = reply.getContent();
        this.parentReply = reply.getParentReply().getId();
    }

}
