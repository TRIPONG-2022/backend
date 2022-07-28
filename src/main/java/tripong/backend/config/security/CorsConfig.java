package tripong.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedHeader("*");
        config.setAllowedHeaders(Arrays.asList("Authorization", "X-Requested-With", "Cache-Control", "Content-Type"));
        config.setAllowedOrigins(Arrays.asList(
//                "https://tripong-development.herokuapp.com/",
                "http://localhost:3000/"
//                "https://127.0.0.1:3000/" //포트번호까지 동일해야할 수 도 있음.
//                "https://tripong.tk/"
        ));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
