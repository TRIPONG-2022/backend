package tripong.backend.dto.post;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.entity.post.Category;

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
    private Integer totalHeadCount;
    private List<MultipartFile> images;
    private MultipartFile thumbnail;
    private Integer budget;

    @Builder
    public PostRequestDto(Long author, String title, String content, String category, List<String> tags, BigDecimal latitude, BigDecimal longitude, LocalDate startDate, LocalDate endDate, Integer totalHeadCount, List<MultipartFile> images, MultipartFile thumbnail, Integer budget) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = Category.valueOf(category);
        this.tags.addAll(tags);
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalHeadCount = totalHeadCount;
        this.images.addAll(images);
        this.thumbnail = thumbnail;
        this.budget = budget;
    }
}
