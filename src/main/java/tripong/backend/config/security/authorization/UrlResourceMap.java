package tripong.backend.config.security.authorization;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Component
public class UrlResourceMap implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private AuthResourceService authResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    public UrlResourceMap(AuthResourceService authResourceService) {
        this.authResourceService = authResourceService;
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {

        log.info("url 리소스 입력 시작");
        if(requestMap == null){
            requestMap = authResourceService.getUrlRequestMap();
        }
        System.out.println("requestMap = " + requestMap);

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
