package tripong.backend.entity.post;

import lombok.*;
import org.hibernate.annotations.*;
import tripong.backend.dto.post.PostRequestDto;
import tripong.backend.entity.BaseTimeEntity;
import tripong.backend.entity.user.User;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @ColumnDefault("0")
    private Integer totalHeadCount;

    @ElementCollection
    @Builder.Default
    private List<String> images = new ArrayList<>();

    private String thumbnail;

    @ColumnDefault("0")
    private Integer budget;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<PostLike> like = new ArrayList<>();

    @ColumnDefault("0")
    private Long viewCount;

    public void update(PostRequestDto postRequestDto, List<String> imageUrlList, String thumbnailUrl) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.tags = postRequestDto.getTags();
        this.latitude = postRequestDto.getLatitude();
        this.longitude = postRequestDto.getLongitude();
        this.startDate = postRequestDto.getStartDate();
        this.endDate = postRequestDto.getEndDate();
        this.totalHeadCount = postRequestDto.getTotalHeadCount();
        this.budget = postRequestDto.getBudget();
        this.images = imageUrlList;
        this.thumbnail = thumbnailUrl;
    }
}
