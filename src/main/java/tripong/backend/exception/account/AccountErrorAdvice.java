package tripong.backend.exception.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.controller.account.AccountController;
import tripong.backend.exception.ErrorResult;


@RestControllerAdvice(assignableTypes = {AccountController.class})
public class AccountErrorAdvice {

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ErrorResult(999, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity accountExceptionHandler(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorResult(code_value(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }

    public int code_value(String message){
        if(message == AccountErrorName.LoginId_DUP){
            return 101;
        }
        else{
            return 999;
        }
    }
}
