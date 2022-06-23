package tripong.backend.dto.account;


import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.RoleType;
import tripong.backend.entity.user.User;


@Data
@NoArgsConstructor
public class OauthJoinRequestDto {

    private String nickName;
    private String loginId;
    private String password;
    private String email;
    private RoleType role;
    private JoinType joinMethod;
    private Integer authentication;

    public User toEntity(){
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .email(email)
                .role(RoleType.Unauth) //추가정보 미입력자
                .joinMethod(joinMethod)
                .authentication(1)
                .build();
    }
}
