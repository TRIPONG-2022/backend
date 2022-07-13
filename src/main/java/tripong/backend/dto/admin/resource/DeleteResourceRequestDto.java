package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResourceRequestDto {

    private String resourceName;
}
