package tripong.backend.exception.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.controller.post.PostApiController;
import tripong.backend.controller.profile.UserProfileApiController;
import tripong.backend.exception.ErrorResult;

import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {UserProfileApiController.class})
public class ProfileErrorAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> noSuchElementExceptionHandler(NoSuchElementException e) {
        ProfileErrorMessage error =  ProfileErrorMessage.valueOf(e.getMessage());
        int code = error.getCode();
        String message = error.getMessage();
        return new ResponseEntity<>(new ErrorResult(code, message), HttpStatus.BAD_REQUEST);
    }
}
