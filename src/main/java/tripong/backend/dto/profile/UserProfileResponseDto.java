package tripong.backend.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tripong.backend.entity.tag.Tag;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    private String loginId;

    private String name;

    private String nickName;

    private String email;

    private String picture;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private GenderType gender;

    private String introduction;

    private String phoneNumber;

    private JoinType joinMethod;

    private Integer authentication;

    private String city;

    private String district;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private List<String> tags = new ArrayList<>();

    @Builder
    public UserProfileResponseDto(User user) {
        this.loginId = user.getLoginId();
        this. name = user.getName();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.introduction = user.getIntroduction();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.introduction = user.getIntroduction();
        this.phoneNumber = user.getPhoneNumber();
        this.joinMethod = user.getJoinMethod();
        this.authentication = user.getAuthentication();
        this.city = user.getCity();
        this.district = user.getDistrict();
        this.latitude = user.getLatitude();
        this.longitude = user.getLongitude();
        this.tags.addAll(user.getTags().stream().map(Tag::getTagName).collect(Collectors.toList()));
    }
}
