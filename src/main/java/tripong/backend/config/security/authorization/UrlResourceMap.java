package tripong.backend.config.security.authorization;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class UrlResourceMap implements FactoryBean<ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private AuthResourceService authResourceService;
    private ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new ConcurrentHashMap<>();

    public UrlResourceMap(AuthResourceService authResourceService) {
        this.authResourceService = authResourceService;
    }

    @Override
    public ConcurrentHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if(requestMap == null){
            requestMap = authResourceService.getUrlRequestMap();
        }
        return requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
