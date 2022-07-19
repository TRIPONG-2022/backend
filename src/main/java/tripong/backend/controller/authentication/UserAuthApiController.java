package tripong.backend.controller.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.authentication.EmailAuthRequestDto;
import tripong.backend.dto.authentication.PasswordRequestDto;
import tripong.backend.dto.authentication.AuthValidationGroup;
import tripong.backend.exception.ErrorResult;
import tripong.backend.exception.authentication.AuthenticationErrorMessage;
import tripong.backend.service.authentication.UserAuthService;
import javax.mail.MessagingException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class UserAuthApiController {

    private final UserAuthService userAuthService;

    // 아이디 찾기
    @PostMapping("/users/auth/find/id")
    public ResponseEntity<Object> findUserId (@Validated @RequestBody EmailAuthRequestDto dto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        String userId = String.valueOf(userAuthService.findUserId(dto).orElseThrow(() -> new NoSuchElementException(AuthenticationErrorMessage.User_NO_SUCH_ELEMENT)));

        return new ResponseEntity<>(userId, HttpStatus.OK);

    }

    // 비밀번호 찾기: 이메일 인증
    @GetMapping("/users/auth/verify-request")
    public ResponseEntity<Object> findUserPassword(@Validated @RequestBody EmailAuthRequestDto dto, BindingResult bindingResult) throws MessagingException {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

       userAuthService.findUserPassword(dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 비밀번호 찾기: 이메일 재인증
    @GetMapping("/users/auth/resend/verify-request")
    public ResponseEntity<Object> ResendfindUserPassword(@Validated @RequestBody EmailAuthRequestDto dto, BindingResult bindingResult) throws MessagingException{

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        userAuthService.verifyResendfindUserPassword(dto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    // 비밀번호 찾기: URL 매핑
    @PatchMapping("/users/auth/reset-password")
    public ResponseEntity<Object> findUserPasswordEmailConfirm(@Validated @RequestBody PasswordRequestDto dto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        userAuthService.verifyfindUserPasswordEmail(dto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    // 비밀번호 바꾸기
    @PatchMapping("/users/auth/change/password")
    public ResponseEntity<Object> changeUserPassword(@Validated(AuthValidationGroup.groupA.class) @RequestBody PasswordRequestDto dto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetail principal){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new ErrorResult(bindingResult), HttpStatus.BAD_REQUEST);
        }

        userAuthService.changeUserPassword(dto, principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
