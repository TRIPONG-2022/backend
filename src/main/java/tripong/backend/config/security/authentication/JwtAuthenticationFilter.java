package tripong.backend.config.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.config.security.authentication.token.TokenService;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.account.NormalLoginRequestDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public void setUsernameParameter(String usernameParameter) {
        super.setUsernameParameter("loginId");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.info("??????: attemptAuthentication");
        ObjectMapper om = new ObjectMapper();
        NormalLoginRequestDto normalLoginDto = null;
        try {
            normalLoginDto = om.readValue(request.getInputStream(), NormalLoginRequestDto.class);
        } catch (Exception e) {
            log.info(e.toString());
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(normalLoginDto.getLoginId(), normalLoginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("??????: attemptAuthentication");
        return authentication;
    }


    //???????????? ??? ??????
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("??????: successfulAuthentication");
        PrincipalDetail principal = (PrincipalDetail)authResult.getPrincipal();
        String roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        tokenService.createTokens(principal.getUser().getId().toString(), principal.getUsername(), roles, request.getHeader("user-agent"), response);
        response.setStatus(HttpServletResponse.SC_OK);

        log.info("??????: successfulAuthentication");
    }
}



