package tripong.backend.config.auth.oauth;

public interface OAuthInfo {
    String getNickName();
    String getEmail();
    String getProviderId();
    String getProviderName();
}
