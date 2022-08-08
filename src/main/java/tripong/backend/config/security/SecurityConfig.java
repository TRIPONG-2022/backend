package tripong.backend.config.security;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tripong.backend.config.security.authentication.token.TokenService;
import tripong.backend.config.security.principal.PrincipalService;
import tripong.backend.config.security.authentication.handler.CustomLoginFailureHandler;
import tripong.backend.config.security.authentication.handler.CustomLogoutHandler;
import tripong.backend.config.security.authentication.JwtAuthenticationFilter;
import tripong.backend.config.security.authentication.JwtAuthorizationFilter;
import tripong.backend.config.security.authentication.token.CookieService;
import tripong.backend.config.security.authorization.*;
import tripong.backend.config.security.oauth.CustomOauthSuccessHandler;
import tripong.backend.config.security.oauth.PrincipalOauth2Service;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends GlobalMethodSecurityConfiguration{

    private final CorsConfig corsConfig;
    private final BCryptPasswordEncoder encoder;
    private final PrincipalOauth2Service oauth2Service;
    private final CookieService cookieService;
    private final PrincipalService principalService;
    private final AuthResourceService authResourceService;
    private final MethodResourceMap methodResourceMap;
    private final TokenService tokenService;
    private final RedisTemplate redisTemplate;

    private static final String[] permitAllResource = {
            "/", "/about", "/posts", "/oauth2/**", "/auth/**", "/error/**"
    };
    private static final String[] SWAGGER_WHITELIST = {"/swagger-resources/**", "/swagger-ui.html", "/v3/api-docs", "/webjars/**"};

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .mvcMatchers(SWAGGER_WHITELIST));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();
        http
                .apply(new MyCustomDsl());
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
                    .httpBasic().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .accessDeniedHandler(customAccessDeniedHandler(http))


                    .and()
                    .logout()
                    .logoutUrl("/users/logout")
//                    .logoutSuccessUrl("/")
                    .addLogoutHandler(new CustomLogoutHandler(cookieService))

                    .and()
                    .oauth2Login()
                    .loginPage("/auth/login")
                    .successHandler(customOauthSuccessHandler(cookieService, redisTemplate))
                    .userInfoEndpoint().userService(oauth2Service);
        }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());

            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, tokenService);
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new CustomLoginFailureHandler());
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, redisTemplate, tokenService);

            CustomFilterSecurityInterceptor customFilterSecurityInterceptor = new CustomFilterSecurityInterceptor(permitAllResource);
            customFilterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
            customFilterSecurityInterceptor.setAuthenticationManager(authenticationManager);
            customFilterSecurityInterceptor.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource(urlResourceMap()));

            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                    .addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        }
    }

    @Bean
    public CustomOauthSuccessHandler customOauthSuccessHandler(CookieService cookieService, RedisTemplate redisTemplate){
        return new CustomOauthSuccessHandler(cookieService, redisTemplate);
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler(HttpSecurity http)  {
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler(http);
        return customAccessDeniedHandler;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setUserDetailsService(principalService);
        daoAuthenticationProvider.setPasswordEncoder(encoder);
        return daoAuthenticationProvider;
    }


    @Bean
    public AccessDecisionManager accessDecisionManager(){
        AffirmativeBased affirmativeBased = new AffirmativeBased(Arrays.asList(new RoleVoter()));
        return affirmativeBased;
    }

    ///
    @Bean
    public CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource(UrlResourceMap urlResourceMap) throws Exception {
        return new CustomFilterInvocationSecurityMetadataSource(urlResourceMap().getObject(), authResourceService);
    }
    private UrlResourceMap urlResourceMap(){
        return new UrlResourceMap(authResourceService);
    }


    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource(); //나중엔 밑에꺼 합쳐보기
    }
    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource(){
        return new MapBasedMethodSecurityMetadataSource(methodResourceMap.getObject());
    }

    @Bean
    public CustomMethodSecurityInterceptor customMethodSecurityInterceptor(MapBasedMethodSecurityMetadataSource methodSecurityMetadataSource){
        CustomMethodSecurityInterceptor customMethodSecurityInterceptor = new CustomMethodSecurityInterceptor();
        customMethodSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        customMethodSecurityInterceptor.setAfterInvocationManager(afterInvocationManager());
        customMethodSecurityInterceptor.setSecurityMetadataSource(methodSecurityMetadataSource);

        RunAsManager runAsManager = runAsManager();
        if(runAsManager != null){
            customMethodSecurityInterceptor.setRunAsManager(runAsManager);
        }

        return customMethodSecurityInterceptor;
    }
}

