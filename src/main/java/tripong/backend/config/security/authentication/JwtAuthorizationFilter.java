package tripong.backend.config.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.authentication.token.JwtProperties;
import tripong.backend.config.security.authentication.token.RefreshTokenProperties;
import tripong.backend.config.security.authentication.token.TokenService;
import tripong.backend.config.security.principal.AuthDetail;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Transactional(readOnly = true)
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private RedisTemplate redisTemplate;
    private TokenService tokenService;
    private String sKey;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate, TokenService tokenService, String sKey){
        super(authenticationManager);
        this.redisTemplate=redisTemplate;
        this.tokenService=tokenService;
        this.sKey = sKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = null;
        String refresh_info = null;
        try{
            refresh_info = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(RefreshTokenProperties.HEADER_STRING)).findFirst().get().getValue();
            jwt = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");

            String pk = JWT.require(Algorithm.HMAC512(sKey)).build().verify(jwt).getClaim("pk").asString();
            String roles = JWT.require(Algorithm.HMAC512(sKey)).build().verify(jwt).getClaim("roles").asString();
            String loginId = JWT.require(Algorithm.HMAC512(sKey)).build().verify(jwt).getClaim("loginId").asString();
            String agent = JWT.require(Algorithm.HMAC512(sKey)).build().verify(refresh_info).getClaim("agent").asString();
            if(redisTemplate.hasKey("RoleUpdate:"+loginId)){
                roles = (String) redisTemplate.opsForValue().get("RoleUpdate:" + loginId);
                tokenService.createTokens(pk, loginId, roles, agent, response);
                redisTemplate.delete("RoleUpdate:"+loginId);
            }
            AuthDetail principal = new AuthDetail(loginId, Long.valueOf(pk), new ArrayList<>(Arrays.asList(roles.split(","))));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenExpiredException tokenExpiredException){
            try {
                String loginId = JWT.require(Algorithm.HMAC512(sKey)).build().verify(refresh_info).getClaim("loginId").asString();
                String pk = JWT.require(Algorithm.HMAC512(sKey)).build().verify(refresh_info).getClaim("pk").asString();
                String roles = JWT.require(Algorithm.HMAC512(sKey)).build().verify(refresh_info).getClaim("roles").asString();
                String agent = JWT.require(Algorithm.HMAC512(sKey)).build().verify(refresh_info).getClaim("agent").asString();
                String uuid = JWT.require(Algorithm.HMAC512(sKey)).build().verify(refresh_info).getClaim("uuid").asString();

                String redis_value = (String) redisTemplate.opsForValue().get("RefreshToken:" + loginId);
                String redis_agent= JWT.require(Algorithm.HMAC512(sKey)).build().verify(redis_value).getClaim("agent").asString();
                String redis_uuid= JWT.require(Algorithm.HMAC512(sKey)).build().verify(redis_value).getClaim("uuid").asString();
                if(!agent.equals(redis_agent) || !uuid.equals(redis_uuid)){
                    chain.doFilter(request, response);
                    return;
                }
                tokenService.createTokens(pk, loginId, roles, request.getHeader("user-agent"), response);
                AuthDetail principal = new AuthDetail(loginId, Long.valueOf(pk), new ArrayList<>(Arrays.asList(roles.split(","))));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception exception){
                chain.doFilter(request, response);
                return;
            }
        } catch (Exception e){
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
