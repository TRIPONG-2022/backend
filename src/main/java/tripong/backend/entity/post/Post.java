package tripong.backend.entity.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import tripong.backend.entity.BaseTimeEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    private List<String> images = new ArrayList<>();

    private String thumbnail;

    @ColumnDefault("0")
    private Integer budget;

    @ColumnDefault("0")
    private Integer recommendationCount;

    @ColumnDefault("0")
    private Integer viewCount;

    @Builder
    public Post(User author, String title, String content, Category category, List<String> tags, BigDecimal latitude, BigDecimal longitude, LocalDate startDate, LocalDate endDate, Integer curHeadCount, Integer totalHeadCount, List<String> images, String thumbnail, Integer budget, Integer recommendationCount, Integer viewCount) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags.addAll(tags);
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.curHeadCount = curHeadCount;
        this.totalHeadCount = totalHeadCount;
        this.images.addAll(images);
        this.thumbnail = thumbnail;
        this.budget = budget;
        this.recommendationCount = recommendationCount;
        this.viewCount = viewCount;
    }
}
