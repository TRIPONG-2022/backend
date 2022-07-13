package tripong.backend.dto.report;


import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.report.ReportType;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PostReportRequestDto {

    @NotNull(message = "신고 범주를 선택해 주세요.")
    private ReportType kind;
}
