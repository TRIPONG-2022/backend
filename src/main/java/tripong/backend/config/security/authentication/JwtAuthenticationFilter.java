package tripong.backend.config.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tripong.backend.config.security.authentication.token.TokenService;
import tripong.backend.config.security.principal.PrincipalDetail;
import tripong.backend.dto.account.NormalLoginRequestDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        NormalLoginRequestDto normalLoginDto = null;
        try {
            normalLoginDto = om.readValue(request.getInputStream(), NormalLoginRequestDto.class);
        } catch (Exception e) {
            log.info(e.toString());
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(normalLoginDto.getLoginId(), normalLoginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetail principal = (PrincipalDetail)authResult.getPrincipal();
        String roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        tokenService.createTokens(principal.getUser().getId().toString(), principal.getUsername(), roles, request.getHeader("user-agent"), response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}




