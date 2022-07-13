package tripong.backend.exception.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;

import java.util.NoSuchElementException;


@RestControllerAdvice("tripong.backend.controller.account")
public class AccountErrorAdvice {

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ErrorResult(999, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity accountExceptionHandler(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorResult(code_value(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity accountPKExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResult(105, e.getMessage()), HttpStatus.OK);
    }

    public int code_value(String message){
        switch (message){
            case AccountErrorMessage.Email_DUP: return 101;
            case AccountErrorMessage.LoginId_DUP: return 102;
            case AccountErrorMessage.NickName_DUP: return 103;
            case AccountErrorMessage.LoginId_NickName_DUP: return 104;
            default: return 999;
        }
    }
}
