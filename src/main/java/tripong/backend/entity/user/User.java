package tripong.backend.entity.user;

import lombok.*;
import tripong.backend.dto.account.FirstExtraInfoPutRequestDto;
import tripong.backend.dto.profile.UserProfileRequestDto;
import tripong.backend.entity.base.BaseTimeEntity;
import tripong.backend.entity.favor.TravelerType;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.tag.Tag;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String loginId; //로그인ID
    @NotNull
    private String password;

    private String name;
    @NotNull
    private String nickName;
    @NotNull
    private String email;

    private String picture;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String introduction;

    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private JoinType joinMethod;

    @NotNull
    private Integer authentication;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    private String city;

    private String district;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Tag> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TravelerType travelerType;

    ////-----편의 메소드-----
    public User(String loginId, String password, String email, String nickName, JoinType joinMethod, Integer authentication){
        this.loginId=loginId;
        this.password=password;
        this.nickName=nickName;
        this.email=email;
        this.joinMethod=joinMethod;
        this.authentication=authentication;
    }

    //추가정보입력
    public void putExtraInfo(FirstExtraInfoPutRequestDto dto){
        this.name = dto.getName();
        this.gender = dto.getGender();
        this.birthDate = dto.getBirthDate();
        this.city = dto.getCity();
        this.district = dto.getDistrict();
    }

    //회원탈퇴시
    public void account_withdrawal(String skey){
        this.name = "탈퇴 회원";
        this.loginId = "탈퇴 회원";
        this.email = this.email+skey;
        this.nickName = "탈퇴 회원";
    }


    //연관관계
    public void addUserRole(List<UserRole> userRoles){
        for(UserRole userRole : userRoles){
            this.userRoles.add(userRole);
            userRole.injectUser(this);
        }
    }


    //init 용
    @Builder
    public User(String loginId, String password, String name, String nickName,
                String email, LocalDate birthDate, GenderType gender, JoinType joinMethod,
                 int authentication, String city, String district, List<UserRole> userRoles) {
        this.loginId=loginId;
        this.password=password;
        this.name=name;
        this.nickName=nickName;
        this.email=email;
        this.birthDate=birthDate;
        this.gender=gender;
        this.joinMethod=joinMethod;
        this.authentication=authentication;
        this.city=city;
        this.district=district;
        this.addUserRole(userRoles);
    }

    public void update(UserProfileRequestDto userProfileRequestDto, String pictureUrl) {
        this.nickName = userProfileRequestDto.getNickName();
        this.picture = pictureUrl;
        this.birthDate = userProfileRequestDto.getBirthDate();
        this.gender = userProfileRequestDto.getGender();
        this.introduction = userProfileRequestDto.getIntroduction();
        this.phoneNumber = userProfileRequestDto.getPhoneNumber();
        this.city = userProfileRequestDto.getCity();
        this.district = userProfileRequestDto.getDistrict();
        this.latitude = userProfileRequestDto.getLatitude();
        this.longitude = userProfileRequestDto.getLongitude();
    }

    // 작성자: 김수연, 용도: 비밀번호 변경
    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    // 작성자: 김수연, 용도: 위도, 경도 변경
    public void changeCurrentLocation(BigDecimal longitude, BigDecimal latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // 작성자: 김수연, 용도: 여행가 유형 변경
    public void updateTravelerType(TravelerType travelerType){
        this.travelerType = travelerType;
    }

}
