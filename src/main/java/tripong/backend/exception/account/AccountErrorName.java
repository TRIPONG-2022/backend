package tripong.backend.exception.account;

public interface AccountErrorName {

    String Email_DUP = "이미 사용중인 이메일 입니다."; //101
    String LoginId_NickName_DUP = "이미 사용중인 아이디와 닉네임 입니다."; //102
    String LoginId_DUP = "이미 사용중인 아이디 입니다."; //103
    String NickName_DUP = "이미 사용중인 닉네임 입니다."; //104

    String PK_NOT_USER = "유저의 데이터가 식별되지 않았습니다. 재시도 바랍니다."; //105

    String LoginId_NOT_MATCH = "존재하지 않는 아이디 입니다."; //106
    String Password_NOT_MATCH = "비밀번호가 틀렸습니다."; //107


    String Undefined_ERROR = "미정의 에러입니다."; //108
}
