package tripong.backend.exception.report;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;

import java.util.NoSuchElementException;

@RestControllerAdvice("tripong.backend.controller.report")
public class ReportErrorAdvice {

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ErrorResult(999, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity reportExceptionHandler(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorResult(code_value(e.getMessage()), e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity reportPKExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResult(601, e.getMessage()), HttpStatus.OK);
    }

    public int code_value(String message){
        switch (message){
            case ReportErrorMessage.MySelf_USER_IMPOSSIBLE: return 602;
            case ReportErrorMessage.MySelf_POST_IMPOSSIBLE: return 603;
            default: return 999;
        }
    }
}
