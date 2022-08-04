package tripong.backend.dto.mate;

import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.favor.TravelerType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MateRequestDto {

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    private TravelerType travelerType;

}
