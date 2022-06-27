package tripong.backend.config.auth.oauth.oauthDetail;

import java.util.Map;

public class NaverUser implements OAuthInfo{

    private Map<String, Object> attribute;
    public NaverUser(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getNickName() {
        return String.valueOf(attribute.get("name"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attribute.get("email"));
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attribute.get("id"));
    }

    @Override
    public String getProviderName() {
        return "naver";
    }
}
