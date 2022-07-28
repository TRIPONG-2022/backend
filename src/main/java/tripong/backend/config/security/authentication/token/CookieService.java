package tripong.backend.config.security.authentication.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CookieService {

    /**
     * refresh 쿠키 추가
     */
    public String refreshCookieIn(String refreshToken){
        log.info("시작: CookieService - 쿠키생성");
        ResponseCookie refreshCookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, refreshToken)
//                .domain("127.0.0.1")
//                .domain("tripong-development.herokuapp.com")
                .domain("localhost:3000")
                .maxAge(RefreshTokenProperties.EXPIRATION_TIME)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        return refreshCookie.toString();
    }


    /**
     * refresh 쿠키 만료 처리
     */
    public String refreshCookieExpired(){
        log.info("시작: CookieService - 쿠키만료");
        ResponseCookie refreshCookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, null)
                .domain("localhost:3000")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        log.info("종료: CookieService - 쿠키만료");
        return refreshCookie.toString();
    }

}
