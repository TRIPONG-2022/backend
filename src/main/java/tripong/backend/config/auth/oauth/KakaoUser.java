package tripong.backend.config.auth.oauth;

import java.util.HashMap;
import java.util.Map;

public class KakaoUser implements OAuthInfo{

    private Map<String, Object> attribute;
    private Map<String, Object> account;
    private Map<String, Object> profile;
    public KakaoUser(Map<String, Object> attribute, Map<String, Object> account, Map<String, Object> profile) {
        this.attribute = attribute;
        this.account = account;
        this.profile=profile;
    }

    @Override
    public String getNickName() {
        return String.valueOf(profile.get("nickname"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(account.get("email"));
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attribute.get("id"));
    }

    @Override
    public String getProviderName() {
        return "kakao";
    }
}
