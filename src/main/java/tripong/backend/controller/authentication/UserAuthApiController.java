package tripong.backend.controller.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.authentication.PasswordRequestDto;
import tripong.backend.dto.authentication.UserAuthRequestDto;
import tripong.backend.exception.authentication.AuthenticationErrorMessage;
import tripong.backend.service.authentication.UserAuthService;
import javax.mail.MessagingException;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserAuthApiController {

    private final UserAuthService userAuthService;

    // 아이디 찾기
    @PostMapping("/users/auth/find/id")
    public ResponseEntity<Object> findUserId (@RequestBody UserAuthRequestDto dto){

        String userId = String.valueOf(userAuthService.findUserId(dto).orElseThrow(() -> new NoSuchElementException(AuthenticationErrorMessage.User_NO_SUCH_ELEMENT)));

        return new ResponseEntity<>(userId, HttpStatus.OK);

    }

    // 비밀번호 찾기: 이메일 인증
    // /users/auth/find/password
    @GetMapping("/auth/verify-request")
    public void findUserPassword(@RequestBody UserAuthRequestDto dto) throws MessagingException {

       userAuthService.findUserPassword(dto);

    }

    // 비밀번호 찾기: 이메일 재인증
    @GetMapping("/auth/verify-request/resend")
    public ResponseEntity<Object> ResendfindUserPassword(@RequestBody UserAuthRequestDto dto) throws MessagingException{

        String result = userAuthService.verifyResendfindUserPassword(dto);

        if(Objects.equals(result, "SUCCESS")){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호 찾기: URL 매핑
    // /users/auth/find/password/confirm
    @PatchMapping("/auth/reset-password")
    public ResponseEntity<Object> findUserPasswordEmailConfirm(@Validated @RequestBody PasswordRequestDto dto){

        String result = userAuthService.verifyfindUserPasswordEmail(dto);

        if (Objects.equals(result, "VERIFY VALID LINK")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호 바꾸기
    @PatchMapping("/users/auth/change/password")
    public ResponseEntity<Object> changeUserPassword(@RequestBody PasswordRequestDto dto){

        userAuthService.changeUserPassword(dto);

       return new ResponseEntity<>(HttpStatus.OK);
    }


}
