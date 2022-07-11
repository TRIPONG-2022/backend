package tripong.backend.dto.post;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostRequestDto {
    @NotNull(message = "작성자는 필수 항목입니다.")
    private Long author;

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @NotNull(message = "내용은 필수 항목입니다.")
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

    private List<MultipartFile> images = new ArrayList<>();

    private MultipartFile thumbnail;

    private Integer budget;

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
