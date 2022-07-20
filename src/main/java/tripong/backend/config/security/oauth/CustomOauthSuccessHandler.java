package tripong.backend.config.security.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tripong.backend.config.security.authentication.token.RefreshTokenProperties;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.config.security.authentication.token.CookieService;
import tripong.backend.config.security.authentication.token.JwtProperties;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomOauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CookieService cookieService;
    private final RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("시작: CustomOauthSuccessHandler");

        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();
        String roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withSubject(JwtProperties.SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis()+ (JwtProperties.EXPIRATION_TIME)))
                .withClaim("pk", principal.getUser().getId().toString())
                .withClaim("roles", roles)
                .withClaim("loginId", principal.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        response.addHeader(JwtProperties.HEADER_STRING, jwtToken);

        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+ RefreshTokenProperties.EXPIRATION_TIME))
                .withClaim("pk", principal.getUser().getId().toString())
                .withClaim("roles", roles)
                .withClaim("loginId", principal.getUsername())
                .withClaim("agent", request.getHeader("user-agent"))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        redisTemplate.opsForValue().set("RefreshToken:"+ principal.getUsername(), refreshToken, RefreshTokenProperties.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        response.addHeader("Set-Cookie", cookieService.refreshCookieIn(refreshToken));
        getRedirectStrategy().sendRedirect(request, response,"http://localhost:3000");
        log.info("종료: CustomOauthSuccessHandler");
    }
}
