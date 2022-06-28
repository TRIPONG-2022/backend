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

        Optional<User> userOptional = userAuthService.findUserId(dto);
        User user;

        if (userOptional.isPresent()){
            user = userOptional.get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호 찾기: 유효 링크 생성
    @ResponseBody
    @GetMapping("/users/auth/find/password")
    public ResponseEntity<Object> findUserPassword(@RequestBody UserAuthRequestDto dto) throws MessagingException {

        String result = userAuthService.findUserPassword(dto);

        if (Objects.equals(result, "FIND USER")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호 찾기: View
    @GetMapping("/users/auth/find/password/view")
    public String findUserPasswordView(){
        return "test/password";
    }

    // 비밀번호 찾기: URL 매핑
    @PatchMapping("/users/auth/find/password/confirm")
    public ResponseEntity<Object> findUserPasswordEmailConfirm(@Validated @RequestBody PasswordRequestDto dto){

        String result = userAuthService.verifyfindUserPasswordEmail(dto);

        if (Objects.equals(result, "VERIFY VALID LINK")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

    }

    // 비밀번호 바꾸기
    @PatchMapping("/users/password/change")
    public ResponseEntity<Object> changeUserPassword(@RequestBody PasswordRequestDto dto){

        String result = userAuthService.changeUserPassword(dto);

        if (Objects.equals(result, "SUCCESS TO CHANGE PASSWORD")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

    }




}
