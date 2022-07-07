package java.tripong.backend.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.user.GenderType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FirstExtraInfoPutRequestDto {

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank
    private String city;

    @NotBlank
    private String district;
}
