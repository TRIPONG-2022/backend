package java.tripong.backend.config.auth.authentication.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tripong.backend.config.auth.authentication.jwt.JwtProperties;

import javax.servlet.http.Cookie;

@Slf4j
@Component
public class JwtCookieService {

    /**
     * jwt 쿠키 추가
     */
    public Cookie jwtCookieIn(String jwtToken){
        log.info("시작: JwtCookieService - 쿠키생성");

        Cookie cookie = new Cookie(tripong.backend.config.auth.authentication.jwt.JwtProperties.HEADER_STRING, jwtToken);
        cookie.setMaxAge(tripong.backend.config.auth.authentication.jwt.JwtProperties.EXPIRATION_TIME);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("localhost");

        log.info("종료: JwtCookieService - 쿠키생성");
        return cookie;
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
