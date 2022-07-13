package tripong.backend.config.security.authorization;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Data
@Component
public class MethodResourceMap implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private AuthResourceService authResourceService;
    private LinkedHashMap<String, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();


    public MethodResourceMap(AuthResourceService authResourceService) {
        this.authResourceService = authResourceService;
    }


    @Override
    public LinkedHashMap<String, List<ConfigAttribute>> getObject(){
        log.info("method 리소스 입력 시작");
        if(requestMap.size() == 0){
            requestMap = authResourceService.getMethodRequestMap();
        }
        System.out.println("메소드 requestMap = " + requestMap);

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
