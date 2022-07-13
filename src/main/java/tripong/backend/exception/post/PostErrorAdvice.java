package tripong.backend.exception.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripong.backend.controller.post.PostApiController;
import tripong.backend.controller.profile.UserProfileApiController;
import tripong.backend.exception.ErrorResult;

import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {PostApiController.class, UserProfileApiController.class})
public class PostErrorAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> illegalExceptionHandler(NoSuchElementException e) {
        PostErrorMessage error =  PostErrorMessage.valueOf(e.getMessage());
        int code = error.getCode();
        String message = error.getMessage();
        return new ResponseEntity<>(new ErrorResult(code, message), HttpStatus.BAD_REQUEST);
    }
}
