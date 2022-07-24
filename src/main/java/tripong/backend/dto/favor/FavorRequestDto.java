package tripong.backend.dto.favor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.favor.Favor;
import tripong.backend.entity.favor.TravelerType;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavorRequestDto {

    @NotEmpty(message = "유저 아이디를 입력해주세요.")
    private String userId;

    private List<Integer> answer = new ArrayList<>();

    private TravelerType travelerType;

    public Favor toEntity() {
        return Favor.builder()
                .answer(answer)
                .travelerType(travelerType)
                .build();
    }


}
