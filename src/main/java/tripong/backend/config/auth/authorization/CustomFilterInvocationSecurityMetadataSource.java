package tripong.backend.config.auth.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    private AuthResourceService authResourceService;

    public CustomFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap, AuthResourceService authResourceService) {
        this.requestMap = requestMap;
        this.authResourceService=authResourceService;
    }


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



    public void reload() throws Exception {
        log.info("리로드");
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = authResourceService.getUrlRequestMap();
        Iterator<Map.Entry<RequestMatcher, List<ConfigAttribute>>> itr = reloadedMap.entrySet().iterator();
        requestMap.clear();

        while (itr.hasNext()) {
            Map.Entry<RequestMatcher, List<ConfigAttribute>> entry = itr.next();
            requestMap.put(entry.getKey(), entry.getValue());
            System.out.println("requestMap = " + requestMap);
        }
    }
}
