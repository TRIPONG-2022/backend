package tripong.backend.exception.mate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.exception.ErrorResult;

import java.util.NoSuchElementException;

@RestControllerAdvice("tripong.backend.controller.mate")
public class MateErrorAdvice {

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ErrorResult(999, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity matePKExceptionHandler(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResult(901, e.getMessage()), HttpStatus.OK);
    }

}
