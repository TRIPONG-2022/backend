package tripong.backend.config.security.authentication.token;

public interface RefreshTokenProperties {
    int EXPIRATION_TIME = (1000*60*60*24*7); //일주일
//    int EXPIRATION_TIME = (10); test
    String HEADER_STRING = "tripong_refresh";

}
