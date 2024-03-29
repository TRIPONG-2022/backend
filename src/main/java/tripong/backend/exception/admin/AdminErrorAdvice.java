package tripong.backend.exception.admin;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;
import tripong.backend.exception.account.AccountErrorMessage;

import java.util.NoSuchElementException;

@RestControllerAdvice("tripong.backend.controller.admin")
public class AdminErrorAdvice {

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ErrorResult(999, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity adminExceptionHandler(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorResult(code_value(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity adminPKExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResult(500, e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity serverDBExceptionHandler(DataIntegrityViolationException e) {
        return new ResponseEntity<>(new ErrorResult(599, AdminErrorMessage.Admin_DB_DUP), HttpStatus.OK);
    }

    public int code_value(String message){
        switch (message){
            case AdminErrorMessage.RoleName_DUP: return 502;
            case AdminErrorMessage.ResourceName_DUP: return 511;
            default: return 999;
        }
    }
}
