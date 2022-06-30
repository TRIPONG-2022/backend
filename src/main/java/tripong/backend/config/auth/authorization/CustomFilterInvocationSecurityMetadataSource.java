package tripong.backend.config.auth.authorization;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    //DB연동 추후

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        if(requestMap != null){
            for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()){
                if(entry.getKey().matches(request)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        this.requestMap.values().forEach(allAttributes::addAll);
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
