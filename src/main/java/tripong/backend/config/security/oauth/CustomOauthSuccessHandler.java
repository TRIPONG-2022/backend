package tripong.backend.config.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import tripong.backend.config.security.authentication.token.TokenService;
import tripong.backend.config.security.principal.PrincipalDetail;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();
        String roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String param = tokenService.oauth_createToken(principal.getUser().getId().toString(), principal.getUsername(), roles, request.getHeader("user-agent"), response);
        String url = UriComponentsBuilder.fromUriString("https://tripong-development.herokuapp.com")
                        .queryParam("token", param).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
