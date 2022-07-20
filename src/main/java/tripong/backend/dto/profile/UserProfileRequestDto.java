package tripong.backend.dto.profile;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tripong.backend.entity.user.GenderType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequestDto {

    private String nickName;

    private MultipartFile picture;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private GenderType gender;

    private String introduction;

    private String phoneNumber;

    private String city;

    private String district;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private List<String> tags = new ArrayList<>();
}
