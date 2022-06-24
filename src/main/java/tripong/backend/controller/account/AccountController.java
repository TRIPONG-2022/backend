package tripong.backend.controller.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tripong.backend.config.auth.jwt.JwtProperties;
import tripong.backend.config.auth.oauth.GoogleUser;
import tripong.backend.dto.account.NormalJoinRequestDto;
import tripong.backend.service.account.AccountService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;


    /**
     * 일반 회원가입 API
     */
    @PostMapping("/users/signup/normal")
    public ResponseEntity normalJoin(@Validated @RequestBody NormalJoinRequestDto dto){
        log.info("시작: AccountController 회원가입" + dto);
        accountService.normalJoin(dto);


        //성공 201,실패 400
        HttpStatus status = HttpStatus.CREATED;
        log.info("종료: AccountController 회원가입");
        return new ResponseEntity<>(status);
    }


    /**
     * 구글 로그인 or 회원가입 API
     */
    @PostMapping("/users/signup/google")
    public ResponseEntity googleJoin(@RequestBody Map<String, Object> data, HttpServletResponse response){
            GoogleUser googleInfo = new GoogleUser((Map<String, Object>) data.get("profileObj"));
            String jwtToken = accountService.googleJoin(googleInfo);

            response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
            HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(status);
    }



}
