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
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class NormalJoinRequestDto {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String nickName;
    @Email
    private String email;
    @NotBlank
    private LocalDate birthDate;
    @NotBlank
    private GenderType gender;
    @NotBlank
    private String address;

    private RoleType role;

    private JoinType joinMethod;

    private Integer authentication;

    @Builder
    public NormalJoinRequestDto(String loginId, String password, String name, String nickName, String email, LocalDate birthDate, GenderType gender, String address, RoleType role, JoinType joinMethod, Integer authentication) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.joinMethod = joinMethod;
        this.authentication = authentication;
    }

    public User toEntity(){
        return User.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .nickName(nickName)
                .email(email)
                .birthDate(birthDate)
                .gender(gender)
                .address(address)
                .role(role)
                .joinMethod(joinMethod)
                .authentication(authentication)
                .build();
    }
}
