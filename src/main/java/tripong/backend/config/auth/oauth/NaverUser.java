package tripong.backend.config.auth.oauth;

import java.util.Map;

public class NaverUser implements OAuthInfo{

    private Map<String, Object> attribute;
    public NaverUser(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getNickName() {
        return (String)attribute.get("name");
    }

    @Override
    public String getEmail() {
        return (String)attribute.get("email");
    }

    @Override
    public String getProviderId() {
        return (String)attribute.get("id");
    }

    @Override
    public String getProviderName() {
        return "naver";
    }
}
