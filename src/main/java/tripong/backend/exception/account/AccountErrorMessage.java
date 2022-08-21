package tripong.backend.exception.account;

public interface AccountErrorMessage {
    //105 = pk 매칭 실패

    String Email_DUP = "이미 사용중인 이메일 입니다."; //101
    String LoginId_DUP = "이미 사용중인 아이디 입니다."; //102
    String NickName_DUP = "이미 사용중인 닉네임 입니다."; //103
    String LoginId_NickName_DUP = "이미 사용중인 아이디와 닉네임 입니다."; //104
    String LoginId_Email_DUP = "이미 사용중인 아이디와 이메일 입니다."; //105
    String NickName_Email_DUP = "이미 사용중인 닉네임과 이메일 입니다."; //106
    String LoginId_NockName_Email_DUP = "이미 사용중인 아이디와 닉네임과 이메일 입니다."; //107
    String SignUp_DB_DUP = "중복확인이 다시 필요합니다."; //108

    String LoginId_NOT_MATCH = "존재하지 않는 아이디 입니다."; //110
    String Password_NOT_MATCH = "비밀번호가 맞지 않습니다."; //111


}
