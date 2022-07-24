package tripong.backend.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.user.GenderType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FirstExtraInfoPutRequestDto {

    @NotBlank(message = "입력이 필요합니다.")
    private String name;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "입력이 필요합니다.")
    private String city;

    @NotBlank(message = "입력이 필요합니다.")
    private String district;
}
