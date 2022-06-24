package tripong.backend.entity.post;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.entity.BaseTimeEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ElementCollection
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Column(precision = 13, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 13, scale = 10)
    private BigDecimal longitude;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @ColumnDefault("0")
    private Integer curHeadCount;

    @ColumnDefault("0")
    private Integer totalHeadCount;

    @ElementCollection
    @Builder.Default
    private List<String> images = new ArrayList<>();

    private String thumbnail;

    @ColumnDefault("0")
    private Integer budget;

    @ColumnDefault("0")
    private Integer recommendationCount;

    @ColumnDefault("0")
    private Integer viewCount;

    public void update(PostRequestDto postRequestDto, List<String> imageUrlList, String thumbnailUrl) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.tags = postRequestDto.getTags();
        this.latitude = postRequestDto.getLatitude();
        this.longitude = postRequestDto.getLongitude();
        this.startDate = postRequestDto.getStartDate();
        this.endDate = postRequestDto.getEndDate();
        this.curHeadCount = postRequestDto.getCurHeadCount();
        this.totalHeadCount = postRequestDto.getTotalHeadCount();
        this.budget = postRequestDto.getBudget();
        this.images = imageUrlList;
        this.thumbnail = thumbnailUrl;
        this.recommendationCount = postRequestDto.getRecommendationCount();
        this.viewCount = postRequestDto.getViewCount();
    }

}
