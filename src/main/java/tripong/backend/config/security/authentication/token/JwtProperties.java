package tripong.backend.config.security.authentication.token;

public interface JwtProperties {
    int EXPIRATION_TIME = (1000*60*5); //5분
//    int EXPIRATION_TIME = 5; // test
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SUBJECT = "Access_JWT";
}