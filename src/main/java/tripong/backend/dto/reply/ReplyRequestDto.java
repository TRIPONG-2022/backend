package tripong.backend.dto.reply;

import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.reply.Reply;
import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
public class ReplyRequestDto {

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    private Long parentReply;

    public Reply toEntity() {
        return Reply.builder()
                .content(content)
                .build();
    }

}