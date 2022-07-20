package tripong.backend.config.security.authentication.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Slf4j
@Component
public class CookieService {

    /**
     * refresh 쿠키 추가
     */
    public String refreshCookieIn(String refreshToken){
        log.info("시작: CookieService - 쿠키생성");
        ResponseCookie refreshCookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, refreshToken)
                .domain("localhost")
//                .domain("tripong-development.herokuapp.com")
                .maxAge(RefreshTokenProperties.EXPIRATION_TIME)
//                .sameSite("None")
//                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        return refreshCookie.toString();
    }


    /**
     * refresh 쿠키 만료 처리
     */
    public Cookie refreshCookieExpired(){
        log.info("시작: CookieService - 쿠키만료");

        Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        log.info("종료: CookieService - 쿠키만료");
        return cookie;
    }

}
