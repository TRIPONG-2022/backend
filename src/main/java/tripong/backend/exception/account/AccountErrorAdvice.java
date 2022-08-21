package tripong.backend.exception.account;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
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
        return new ResponseEntity<>(new ErrorResult(100, e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity serverDBExceptionHandler(DataIntegrityViolationException e) {
        return new ResponseEntity<>(new ErrorResult(108, AccountErrorMessage.SignUp_DB_DUP), HttpStatus.OK);
    }

    public int code_value(String message){
        switch (message){
            case AccountErrorMessage.Email_DUP: return 101;
            case AccountErrorMessage.LoginId_DUP: return 102;
            case AccountErrorMessage.NickName_DUP: return 103;
            case AccountErrorMessage.LoginId_NickName_DUP: return 104;
            case AccountErrorMessage.LoginId_Email_DUP: return 105;
            case AccountErrorMessage.NickName_Email_DUP: return 106;
            case AccountErrorMessage.LoginId_NockName_Email_DUP: return 107;
            default: return 999;
        }
    }
}
