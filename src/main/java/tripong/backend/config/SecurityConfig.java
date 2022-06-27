package tripong.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tripong.backend.config.auth.PrincipalService;
import tripong.backend.config.auth.handler.CustomLoginFailureHandler;
import tripong.backend.config.auth.handler.CustomLogoutHandler;
import tripong.backend.config.auth.jwt.JwtAuthenticationFilter;
import tripong.backend.config.auth.jwt.JwtAuthorizationFilter;
import tripong.backend.config.auth.oauth.CustomOauthSuccessHandler;
import tripong.backend.config.auth.oauth.PrincipalOauth2Service;
import tripong.backend.repository.user.UserRepository;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CorsConfig corsConfig;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PrincipalOauth2Service oauth2Service;
    private final CustomOauthSuccessHandler customOauthSuccessHandler;
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**"
    };
    private final PrincipalService principalService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(SWAGGER_WHITELIST));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/users/**", "/auth/**", "/test/**", "/error/**", "/login/**", "/swagger-ui/**").permitAll()
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .anyRequest().authenticated();
        http
                .apply(new MyCustomDsl())

                .and()
                .logout()
                .logoutUrl("/users/logout")
                .addLogoutHandler(new CustomLogoutHandler())

                .and()
                .oauth2Login()
                .loginPage("/") // "/auth/login" -> 테스트위해 잠시 "/" 으로 변경
                .successHandler(customOauthSuccessHandler)
                .userInfoEndpoint().userService(oauth2Service);

        return http.build();
    }


    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void init(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .formLogin().disable()
                    .httpBasic().disable();
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());

            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new CustomLoginFailureHandler());
            jwtAuthenticationFilter.setFilterProcessesUrl("/users/login");

            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(jwtAuthenticationFilter)
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setUserDetailsService(principalService);
        daoAuthenticationProvider.setPasswordEncoder(encoder);
        return daoAuthenticationProvider;
    }
}

