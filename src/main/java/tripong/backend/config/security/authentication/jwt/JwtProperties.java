package tripong.backend.config.security.authentication.jwt;

public interface JwtProperties {
    String SECRET = "tripong_secret_key";
    int EXPIRATION_TIME = (1000*60*60); //60분
//    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}