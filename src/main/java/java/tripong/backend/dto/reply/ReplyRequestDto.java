package java.tripong.backend.dto.reply;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.reply.Reply;


@Data
@NoArgsConstructor
public class ReplyRequestDto {

    private Long id;
    private Long postId;
    private String userId;
    private String content;
    private Long parentReply;

    @Builder
    public ReplyRequestDto(Long id, Long postId, String userId, String content, Long parentReply){
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.parentReply = parentReply;
    }

    public Reply toEntity() {
        return Reply.builder()
                .id(id)
                .postId(postId)
                .content(content)
                .parentReply(parentReply)
                .build();
    }

}