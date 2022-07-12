package tripong.backend.exception.report;

public interface ReportErrorName {

    String PK_NOT_REPORTED = "신고대상 데이터가 식별되지 않았습니다. 재시도 바랍니다."; //601

    String MySelf_USER_IMPOSSIBLE = "자기 자신은 신고가 불가합니다."; //602
    String MySelf_POST_IMPOSSIBLE = "자신의 게시글은 신고가 불가합니다."; //603


}
