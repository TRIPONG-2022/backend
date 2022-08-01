package tripong.backend.dto.mate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.favor.TravelerType;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static tripong.backend.entity.user.QUser.user;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MateResponseDto {

    private Long id;

    private String loginId;

    private String name;

    private String nickName;

    private String picture;

    private Long koreanAge;

    private Double distance;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String introduction;

    private String city;

    private String district;

    private TravelerType travelerType;

}

