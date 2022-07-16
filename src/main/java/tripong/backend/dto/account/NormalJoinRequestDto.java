package tripong.backend.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class NormalJoinRequestDto {

    @Size(min=2, max= 11, message = "2자 이상, 11자 이하로 설정해 주세요.") //소셜 자동 생성 닉네임과 중복고려
    private String nickName;

    @Size(min=5, max= 11, message = "5자 이상, 11자 이하로 설정해 주세요.") //소셜 자동 생성 아이디와 중복고려
    private String loginId;

    @Size(min=4, message = "4자 이상, 15자 이하로 설정해 주세요.")
    private String password;

    @Email(message = "이메일 양식을 지켜주세요.")
    private String email;
}

