package tripong.backend.dto.admin.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserAllListDto {

    private Long id;
    private String name;
    private String loginId;
    private String nickName;
    private List<UserRolesDto> roles;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    public GetUserAllListDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.nickName = user.getNickName();
        this.createdDate = user.getCreatedDate();
        this.roles = user.getUserRoles().stream()
                .map(ur -> new UserRolesDto(ur)).collect(Collectors.toList());
    }

}
