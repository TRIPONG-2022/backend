package tripong.backend.exception.admin;

public interface AdminErrorName {

    String Role_FORM_ERROR = "ROLE_~~ 으로 작성해 주세요."; //501
    String PK_NOT_ROLE = "권한 데이터가 식별되지 않았습니다. 재시도 바랍니다."; //502


    String ResourceName_DUP = "이미 등록된 자원입니다."; //510
    String PK_NOT_USER = "유저 데이터가 식별되지 않았습니다. 재시도 바랍니다."; //511

}
