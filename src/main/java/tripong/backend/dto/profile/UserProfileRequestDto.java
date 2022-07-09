package tripong.backend.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
