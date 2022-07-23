package tripong.backend.entity.report;

import lombok.Getter;

@Getter
public enum ReportType {
    Abuse("Abuse","욕설"),
    Ads("Ads","광고/홍보"),
    Spam("Spam","도배/스팸"),
    Impropriety("Impropriety","게시판부적절"),
    Etc("Etc","기타");
    private final String en;
    private final String kr;
    ReportType(String en, String kr) {
        this.en = en;
        this.kr = kr;
    }
}
