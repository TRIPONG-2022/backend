package tripong.backend.exception.authentication;

public interface AuthenticationErrorMessage {

    // 702
    String Email_Valid_Link_EXPIRED = "이메일 유효 링크 시간이 경과되었습니다.";
    // 703
    String Resend_Email_Auth_FAIL = "이메일 재인증이 실패하였습니다.";
    // 704
    String Gmail_SMTP_ERROR = "이메일 전송에 실패하였습니다.";
    // 705
    String Change_Password_FAIL = "비밀번호 변경에 실패하였습니다.";


}
