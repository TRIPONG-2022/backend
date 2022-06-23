package tripong.backend.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {

    private Long id;

    private String author;

    private String title;

    private String content;

    private Category category;

    private List<String> tags = new ArrayList<>();

    private BigDecimal latitude;

    private BigDecimal longitude;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer curHeadCount;

    private Integer totalHeadCount;

    private List<String> images = new ArrayList<>();

    private String thumbnail;

    private Integer budget;

    private Integer recommendationCount;

    private Integer viewCount;

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
        this.curHeadCount = post.getCurHeadCount();
        this.totalHeadCount = post.getTotalHeadCount();
        this.budget = post.getBudget();
        this.recommendationCount = post.getRecommendationCount();
        this.viewCount = post.getViewCount();
    }
}
