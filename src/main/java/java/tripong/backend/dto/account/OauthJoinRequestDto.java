package java.tripong.backend.dto.account;


import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;

import java.util.List;


@Data
@NoArgsConstructor
public class OauthJoinRequestDto {

    private String nickName;
    private String loginId;
    private String password;
    private String email;
    private JoinType joinMethod;
    private Integer authentication;
    private List<UserRole> userRoles;

    public User toEntity(){
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .email(email)
                .joinMethod(joinMethod)
                .authentication(1) //소셜로그인으로 인한 이메일 인증처리
                .userRoles(userRoles)
                .build();
    }
}
