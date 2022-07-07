package java.tripong.backend.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto implements Serializable {

    private Long id;

    private String author;

    private String title;

    private String content;

    private Category category;

    private List<String> tags = new ArrayList<>();

    private BigDecimal latitude;

    private BigDecimal longitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer curHeadCount;

    private Integer totalHeadCount;

    private List<String> images = new ArrayList<>();

    private String thumbnail;

    private Integer budget;

    private Integer likeCount;

    private Long viewCount;

    @Builder
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor().getName();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.tags.addAll(post.getTags());
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();
        this.curHeadCount = post.getGatheringUsers().size();
        this.totalHeadCount = post.getTotalHeadCount();
        this.thumbnail = post.getThumbnail();
        this.budget = post.getBudget();
        this.likeCount = post.getLike().size();
        this.viewCount = post.getViewCount();
    }
}
