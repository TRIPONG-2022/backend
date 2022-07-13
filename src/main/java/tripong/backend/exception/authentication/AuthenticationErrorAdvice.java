package tripong.backend.exception.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;

import java.util.NoSuchElementException;

@RestControllerAdvice("tripong.backend.controller.authentication")
public class AuthenticationErrorAdvice {

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
        return new ResponseEntity<>(new ErrorResult(701, e.getMessage()), HttpStatus.OK);
    }

    public int code_value(String message){
        switch (message){
            case AuthenticationErrorMessage.Email_Valid_Link_EXPIRED: return 702;
            case AuthenticationErrorMessage.Resend_Email_Auth_FAIL: return 703;
            case AuthenticationErrorMessage.Gmail_SMTP_ERROR: return 704;
            case AuthenticationErrorMessage.Change_Password_FAIL:return 705;
            default: return 999;
        }
    }

}
