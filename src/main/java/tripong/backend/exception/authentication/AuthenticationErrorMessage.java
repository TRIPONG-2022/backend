package tripong.backend.exception.authentication;

public interface AuthenticationErrorMessage {

    // 701
    String User_NO_SUCH_ELEMENT = "해당 유저가 존재하지 않습니다..";
    // 702
    String Email_Valid_Link_NO_SUCH_ELEMENT = "이메일 유효 링크가 존재하지 않습니다.";
    // 703
    String Email_Valid_Link_EXPIRED = "이메일 유효 링크 시간이 경과되었거나 유효하지 않습니다.";



}
