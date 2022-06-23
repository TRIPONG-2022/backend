package tripong.backend.config.auth.oauth;

import java.util.Map;

public class GoogleUser implements OAuthInfo{

    private Map<String, Object> attribute;
    public GoogleUser(Map<String, Object> attribute) {
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
        return (String)attribute.get("sub");
    }

    @Override
    public String getProviderName() {
        return "google";
    }
}
