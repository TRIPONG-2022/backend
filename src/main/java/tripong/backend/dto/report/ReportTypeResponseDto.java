package tripong.backend.dto.report;

import lombok.Data;

@Data
public class ReportTypeResponseDto {

    private String en;
    private String kr;

    public ReportTypeResponseDto(String en, String kr){
        this.en = en;
        this.kr = kr;
    }
}
