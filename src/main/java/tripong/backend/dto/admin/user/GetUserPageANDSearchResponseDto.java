package tripong.backend.dto.admin.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import tripong.backend.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetUserPageANDSearchResponseDto {

    private Long id;
    private String name;
    private String loginId;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private List<UserRolesDto> roles;

    public GetUserPageANDSearchResponseDto(){}

    @QueryProjection
    public GetUserPageANDSearchResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.nickName = user.getNickName();
        this.createdDate = user.getCreatedDate();
        this.roles = user.getUserRoles().stream().map(ur -> new UserRolesDto(ur)).collect(Collectors.toList());
    }


}