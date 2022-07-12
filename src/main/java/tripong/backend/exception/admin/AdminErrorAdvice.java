package tripong.backend.exception.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;

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

    public int code_value(String message){
        switch (message){
            case AdminErrorName.Role_FORM_ERROR: return 501;
            case AdminErrorName.PK_NOT_ROLE: return 502;
            case AdminErrorName.ResourceName_DUP: return 510;
            case AdminErrorName.PK_NOT_USER: return 511;
            default: return 999;
        }
    }
}
