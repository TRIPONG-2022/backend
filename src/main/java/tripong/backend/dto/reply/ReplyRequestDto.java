package tripong.backend.dto.reply;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.reply.Reply;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReplyRequestDto {

    private BigInteger postId;

    private BigInteger userId;

    private String content;

    private Reply parentReply;

    private List<Reply> childrenReply = new ArrayList<>();

    @Builder
    public ReplyRequestDto(BigInteger postId, BigInteger userId, String content, Reply parentReply, List<Reply> childrenReply){
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.parentReply = parentReply;
        this.childrenReply = childrenReply;
    }

    public Reply toEntity() {
        return Reply.builder()
                .postId(postId)
                .userId(userId)
                .content(content)
                .parentReply(parentReply)
                .childrenReply(childrenReply)
                .build();
    }

}
