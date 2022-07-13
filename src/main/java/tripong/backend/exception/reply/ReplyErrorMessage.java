package tripong.backend.exception.reply;

public interface ReplyErrorMessage {
    // 401
    String PostId_NOT_MATCH = "존재하지 않는 게시물입니다.";
    // 402
    String LoginId_NOT_MATCH = "존재하지 않는 아이디입니다.";
    // 403
    String ReplyId_NOT_MATCH = "존재하지 않는 댓글입니다.";
    // 404
    String ParentReply_NOT_MATCH = "존재하지 않는 부모댓글입니다.";

}
