package tripong.backend.controller.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tripong.backend.dto.authorization.EmailAuthRequestDto;
import tripong.backend.service.authorization.EmailAuthService;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    // 이메일 유효링크 인증
    @GetMapping("/users/auth/email/send")
    public void emailAuth(@RequestBody EmailAuthRequestDto dto) throws MessagingException {

        // 이메일 유효 링크 서비스 호출
        emailAuthService.createEmailValidLik(dto);

    }

    // 유효 링크 매핑
    @GetMapping("/users/auth/email/confirm")
    public ResponseEntity<Integer> emailConfirm(@Validated @RequestParam String emailValidLink){

        // 이메일 유효 링크 확인 서비스 호출
        int emailAuthConfrim = emailAuthService.emailConfirm(emailValidLink);

        if(emailAuthConfrim == 1){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
