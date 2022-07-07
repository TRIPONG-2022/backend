package java.tripong.backend.dto.account;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class NormalJoinRequestDto {

    @NotBlank
    @Size(min=2, max= 11, message = "닉네임은 2자 이상, 11자 이하 입니다.") //소셜 자동 생성 닉네임과 중복고려
    private String nickName;

    @NotBlank
    @Size(min=2, max= 11, message = "아이디는 2자 이상, 11자 이하 입니다.") //소셜 자동 생성 아이디와 중복고려
    private String loginId;

    @NotBlank
    @Size(min=4, max=15, message = "비밀번호는 4자 이상, 15자 이하 입니다.")
    private String password;

    @Email(message = "이메일 양식을 지켜주세요.")
    private String email;



    @Builder
    public NormalJoinRequestDto(String nickName, String loginId, String password, String email) {
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
                .joinMethod(JoinType.Normal)
                .build();
    }
}

