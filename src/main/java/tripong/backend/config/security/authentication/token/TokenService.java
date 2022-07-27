package tripong.backend.config.security.authentication.token;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenService {
    private final RedisTemplate redisTemplate;
    private final CookieService cookieService;

    public void createTokens (String pk, String loginId, String roles, String agent, HttpServletResponse response){
        String jwtToken = JWT.create()
                .withSubject(JwtProperties.SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis()+ (JwtProperties.EXPIRATION_TIME)))
                .withClaim("pk", pk)
                .withClaim("roles", roles)
                .withClaim("loginId", loginId)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
        String uuid = UUID.randomUUID().toString().substring(0,10);
        String refreshToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+ RefreshTokenProperties.EXPIRATION_TIME))
                .withClaim("pk", pk)
                .withClaim("roles", roles)
                .withClaim("loginId", loginId)
                .withClaim("agent", agent)
                .withClaim("uuid", uuid)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        redisTemplate.opsForValue().set("RefreshToken:"+ loginId, refreshToken, RefreshTokenProperties.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        response.addHeader("Set-Cookie", cookieService.refreshCookieIn(refreshToken));

    }
}
