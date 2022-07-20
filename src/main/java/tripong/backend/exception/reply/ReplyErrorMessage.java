package tripong.backend.exception.reply;

public interface ReplyErrorMessage {
    // 401
    String PostId_NO_SUCH_ELEMENT = "존재하지 않는 게시물입니다.";
    // 402
    String LoginId_NO_SUCH_ELEMENT = "존재하지 않는 아이디입니다.";
    // 403
    String ReplyId_NO_SUCH_ELEMENT = "존재하지 않는 댓글입니다.";
    // 404
    String ParentReply_NO_SUCH_ELEMENT = "존재하지 않는 부모댓글입니다.";
}
