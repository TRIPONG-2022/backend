package tripong.backend.controller.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tripong.backend.dto.authorization.EmailAuthRequestDto;
import tripong.backend.service.authorization.EmailAuthService;

@Controller
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    // 이메일 유효링크 인증
    @ResponseBody
    @GetMapping("/users/auth/email/send")
    public void emailAuth(@RequestBody EmailAuthRequestDto earDto){

        // 이메일 유효 링크 서비스 호출
        emailAuthService.createEmailValidLik(earDto);

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
