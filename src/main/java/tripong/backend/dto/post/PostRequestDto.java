package tripong.backend.dto.post;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostRequestDto {
    private Long author;
    private String title;
    private String content;
    private Category category;
    private List<String> tags = new ArrayList<>();
    private BigDecimal latitude;
    private BigDecimal longitude;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer curHeadCount;
    private Integer totalHeadCount;
    private List<MultipartFile> images;
    private MultipartFile thumbnail;
    private Integer budget;
    private Integer recommendationCount;
    private Long viewCount;

    @Builder
    public PostRequestDto(Long author, String title, String content, Category category, List<String> tags, BigDecimal latitude, BigDecimal longitude, LocalDate startDate, LocalDate endDate, Integer curHeadCount, Integer totalHeadCount, List<MultipartFile> images, MultipartFile thumbnail, Integer budget, Integer recommendationCount, Long viewCount) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.curHeadCount = curHeadCount;
        this.totalHeadCount = totalHeadCount;
        this.images = images;
        this.thumbnail = thumbnail;
        this.budget = budget;
        this.recommendationCount = recommendationCount;
        this.viewCount = viewCount;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .tags(tags)
                .latitude(latitude)
                .longitude(longitude)
                .startDate(startDate)
                .endDate(endDate)
                .totalHeadCount(totalHeadCount)
                .budget(budget)
                .build();
    }
}
