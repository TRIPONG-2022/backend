package tripong.backend.controller.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tripong.backend.dto.authorization.EmailAuthRequestDto;
import tripong.backend.service.authorization.EmailAuthService;

@Controller
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    // 이메일 유효링크 인증
    @GetMapping("/users/auth/email/send/{email}")
    public ResponseEntity emailAuth(@RequestBody EmailAuthRequestDto earDto){

        // 성공 302, 실패 400
        HttpStatus status = HttpStatus.FOUND;

        return new ResponseEntity<>(status);
    }

    // 유효 링크 매핑
    @GetMapping("/users/auth/email/confirm")
    public ResponseEntity emailConfirm(@RequestParam String emailValidLink){

        HttpStatus status = HttpStatus.FOUND;

        return new ResponseEntity<>(status);
    }
}
