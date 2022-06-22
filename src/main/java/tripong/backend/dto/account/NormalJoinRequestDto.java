package tripong.backend.dto.account;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.user.User;
import tripong.backend.entity.user.GenderType;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class NormalJoinRequestDto {

    @NotBlank
    @Size(max= 11, message = "닉네임은 11자 이하") //소셜 자동 생성 닉네임과 중복고려
    private String nickName;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @Email
    private String email;


    @Builder
    public NormalJoinRequestDto(String nickName, String loginId, String password,  String email) {
        this.nickName = nickName;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
    }

    public User toEntity(){
        return User.builder()
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .email(email)
                .role(RoleType.User)
                .build();
    }
}

