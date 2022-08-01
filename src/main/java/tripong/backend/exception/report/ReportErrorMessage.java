package tripong.backend.exception.report;

public interface ReportErrorMessage {
    //600 = pk 매칭 실패
    String MySelf_USER_IMPOSSIBLE = "자기 자신은 신고가 불가합니다."; //602
    String MySelf_POST_IMPOSSIBLE = "자신의 게시글은 신고가 불가합니다."; //603
}
