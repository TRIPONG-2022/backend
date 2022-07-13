package tripong.backend.exception.reply;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;
import java.util.NoSuchElementException;

@RestControllerAdvice("tripong.backend.controller.reply")
public class ReplyErrorAdvice {

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ErrorResult(999, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity accountExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResult(code_value(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }

    public int code_value(String message){
        switch(message){
            case ReplyErrorMessage.PostId_NOT_MATCH: return 401;
            case ReplyErrorMessage.LoginId_NOT_MATCH: return 402;
            case ReplyErrorMessage.ReplyId_NOT_MATCH: return 403;
            case ReplyErrorMessage.ParentReply_NOT_MATCH:return 404;
            default: return 999;
        }
    }

}
