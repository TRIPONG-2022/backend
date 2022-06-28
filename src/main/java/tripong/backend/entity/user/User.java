package tripong.backend.entity.user;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tripong.backend.entity.base.BaseTimeEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String loginId; //로그인ID

    private String password;

    private String name;

    private String nickName;

    private String email;

    private String picture;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String introduction;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private JoinType joinMethod;

    @ColumnDefault("0")
    private Integer authentication;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Builder
    public User(String loginId, String password, String name, String nickName,
                String email, LocalDate birthDate, GenderType gender, JoinType joinMethod,
                 int authentication, RoleType role, String address) {
        this.loginId=loginId;
        this.password=password;
        this.name=name;
        this.nickName=nickName;
        this.email=email;
        this.birthDate=birthDate;
        this.gender=gender;
        this.joinMethod=joinMethod;
        this.authentication=authentication;
        this.role=role;
        this.address=address;
    }


}
