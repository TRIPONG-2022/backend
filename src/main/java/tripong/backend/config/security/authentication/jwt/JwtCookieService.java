package tripong.backend.config.security.authentication.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Slf4j
@Component
public class JwtCookieService {

    /**
     * jwt 쿠키 추가
     */
    public String jwtCookieIn(String jwtToken){
        log.info("시작: JwtCookieService - 쿠키생성");
        ResponseCookie jwtCookie = ResponseCookie.from(JwtProperties.HEADER_STRING, jwtToken)
                .maxAge(JwtProperties.EXPIRATION_TIME)
//                .sameSite("None")
//                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        return jwtCookie.toString();
    }


    /**
     * jwt 쿠키 만료 처리
     */
    public Cookie jwtCookieExpired(){
        log.info("시작: JwtCookieService - 쿠키만료");

        Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        log.info("종료: JwtCookieService - 쿠키만료");
        return cookie;
    }

}
