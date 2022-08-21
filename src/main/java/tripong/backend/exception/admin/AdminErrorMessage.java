package tripong.backend.exception.admin;

public interface AdminErrorMessage {
    //500 = pk 매칭 실패
    String RoleName_DUP = "이미 등록된 권한입니다."; //502


    String ResourceName_DUP = "이미 등록된 자원입니다."; //510

    String Admin_DB_DUP = "중복 확인이 필요합니다."; //599
}
