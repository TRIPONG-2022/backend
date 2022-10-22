package tripong.backend.dto.admin.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.post.Category;

import java.time.LocalDateTime;

@Data
public class GetPostPageANDSearchResponseDto {

    private Long userId;
    private Long postId;
    private String title;
    private String loginId;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime postCreatedDate;
    private Category category;

    public GetPostPageANDSearchResponseDto(){}

    @QueryProjection
    public GetPostPageANDSearchResponseDto(Long userId, Long postId, String title, String loginId, String nickName, LocalDateTime createdDate, Category category){
        this.userId = userId;
        this.postId = postId;
        this.title = title;
        this.loginId = loginId;
        this.nickName=nickName;
        this.postCreatedDate = createdDate;
        this.category=category;
    }
}
