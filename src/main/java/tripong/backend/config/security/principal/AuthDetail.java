package tripong.backend.config.security.principal;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class AuthDetail implements UserDetails, OAuth2User {
    private String loginId;
    private Long pk;
    private List<String> roles = new ArrayList<>();
    private Map<String, Object> attributes;


    public AuthDetail(String loginId, Long pk, List<String> roles){ //Normal
        this.loginId = loginId;
        this.pk = pk;
        this.roles = roles;
    }
    public AuthDetail(String loginId, Long pk, List<String> roles, Map<String, Object> attributes) { //OAuth
        this.loginId = loginId;
        this.pk = pk;
        this.roles = roles;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        roles.forEach(r -> collectors.add(new SimpleGrantedAuthority(r)));
        return collectors;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() { //소셜로그인시의 닉네임 or 이름
        return String.valueOf(attributes.get("name"));
    }
}
