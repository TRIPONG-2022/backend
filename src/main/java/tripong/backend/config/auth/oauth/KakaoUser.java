package tripong.backend.config.auth.oauth;

import java.util.Map;

public class KakaoUser implements OAuthInfo{

    private Map<String, Object> attribute;
    public KakaoUser(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) account.get("profile");


    @Override
    public String getNickName() {
        return (String)profile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String)account.get("email");
    }

    @Override
    public String getProviderId() {
        return (String)attribute.get("id");
    }

    @Override
    public String getProviderName() {
        return "kakao";
    }
}
