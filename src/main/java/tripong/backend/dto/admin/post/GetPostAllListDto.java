package tripong.backend.dto.admin.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tripong.backend.entity.post.Post;

import java.time.LocalDateTime;

@Data
public class GetPostAllListDto {

    private Long userId;
    private Long postId;
    private String title;
    private String loginId;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    public GetPostAllListDto(Post post){
        this.userId = post.getAuthor().getId();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.loginId = post.getAuthor().getLoginId();
        this.nickName= post.getAuthor().getNickName();
        this.createdDate = post.getCreatedDate();
    }
}
