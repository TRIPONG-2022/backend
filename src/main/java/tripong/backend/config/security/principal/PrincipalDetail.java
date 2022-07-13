package tripong.backend.config.security.principal;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetail(User user){ //Normal
        this.user=user;
    }
    public PrincipalDetail(User user, Map<String, Object> attributes) { //OAuth
        this.user = user;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        for(UserRole userRole : user.getUserRoles()){
            collectors.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
            System.out.println("userRole = " + userRole.getRole().getRoleName());
        }
        return collectors;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
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
