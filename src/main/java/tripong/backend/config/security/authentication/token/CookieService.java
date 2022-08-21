package tripong.backend.config.security.authentication.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;


@Component
public class CookieService {

    /**
     * refresh 쿠키 추가
     */
    public String refreshCookieIn(String refreshToken){
        ResponseCookie refreshCookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, refreshToken)
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
        ResponseCookie refreshCookie = ResponseCookie.from(RefreshTokenProperties.HEADER_STRING, null)
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        return refreshCookie.toString();
    }

}
