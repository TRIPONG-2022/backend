package tripong.backend.controller.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.authentication.PasswordRequestDto;
import tripong.backend.dto.authentication.UserAuthRequestDto;
import tripong.backend.entity.user.User;
import tripong.backend.service.authentication.UserAuthService;
import javax.mail.MessagingException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserAuthRestController {

    private final UserAuthService userAuthService;

    // 아이디 찾기
    @ResponseBody
    @PostMapping("/users/auth/find/id")
    public ResponseEntity<Object> findUserId (@RequestBody UserAuthRequestDto dto){

        String userId = String.valueOf(userAuthService.findUserId(dto).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. 이메일을 확인해주세요. emial=" + dto.getEmail())));

        return new ResponseEntity<>(userId, HttpStatus.OK);

    }

    // 비밀번호 찾기: 유효 링크 생성\
    // /users/auth/find/password
    @ResponseBody
    @GetMapping("/auth/verify-request")
    public ResponseEntity<Object> findUserPassword(@RequestBody UserAuthRequestDto dto) throws MessagingException {

       userAuthService.findUserPassword(dto);

       return new ResponseEntity<>(HttpStatus.OK);

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
    @PatchMapping("/auth/reset-password")
    public ResponseEntity<Object> changeUserPassword(@RequestBody PasswordRequestDto dto){

        String result = userAuthService.changeUserPassword(dto);

        if (Objects.equals(result, "SUCCESS TO CHANGE PASSWORD")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

    }




}
