package tripong.backend.config.security.oauth.oauthDetail;

import java.util.Map;

public class GoogleUser implements OAuthInfo{

    private Map<String, Object> attribute;
    public GoogleUser(Map<String, Object> attribute) {
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
        return String.valueOf(attribute.get("sub"));
    }

    @Override
    public String getProviderName() {
        return "google";
    }
}
