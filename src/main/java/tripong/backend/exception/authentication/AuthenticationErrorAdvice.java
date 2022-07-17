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
    public ResponseEntity authenticationExceptionHandler(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorResult(code_value_illegal(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity authenticationPKExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResult(code_value_noSuchElement(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }

    public int code_value_illegal(String message){
        switch (message){
            case AuthenticationErrorMessage.Email_Valid_Link_EXPIRED: return 703;
            case AuthenticationErrorMessage.Resend_Email_Auth_FAIL: return 704;
            default: return 999;
        }
    }

    public int code_value_noSuchElement(String message) {
        switch (message){
            case AuthenticationErrorMessage.User_NO_SUCH_ELEMENT: return 701;
            case AuthenticationErrorMessage.Email_Valid_Link_NO_SUCH_ELEMENT: return 702;
            default: return 999;
        }
    }

}
