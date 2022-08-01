package tripong.backend.dto.admin.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRolesRequestDto {

    @NotNull(message = "입력이 필요합니다.")
    private List<String> roleNames;
}
