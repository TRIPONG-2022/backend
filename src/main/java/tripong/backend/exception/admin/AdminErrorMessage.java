package tripong.backend.exception.admin;

public interface AdminErrorMessage {
    //502 = pk 매칭 실패
    String Role_FORM_ERROR = "ROLE_~~ 으로 작성해 주세요."; //501
    String ResourceName_DUP = "이미 등록된 자원입니다."; //510
}