package tripong.backend.config.auth.oauth;

import java.util.Map;

public class FacebookUser implements OAuthInfo{

    private Map<String, Object> attribute;
    public FacebookUser(Map<String, Object> attribute) {
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
        return "facebook";
    }
}
