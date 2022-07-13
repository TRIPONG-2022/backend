package tripong.backend.config.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;


import java.util.*;

@Slf4j
@Component
public class MethodResourceLiveUpdateService {

    private AnnotationConfigServletWebServerApplicationContext applicationContext;
    private CustomMethodSecurityInterceptor customMethodSecurityInterceptor;
    private MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;
    @Autowired ApplicationContext ac;

    public MethodResourceLiveUpdateService(AnnotationConfigServletWebServerApplicationContext applicationContext, CustomMethodSecurityInterceptor customMethodSecurityInterceptor, MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource) {
        this.applicationContext = applicationContext;
        this.customMethodSecurityInterceptor = customMethodSecurityInterceptor;
        this.mapBasedMethodSecurityMetadataSource = mapBasedMethodSecurityMetadataSource;
    }

    private Map<String, ProxyFactory> advisorMap = new HashMap<>(); //키= 클래스, 값 = 어드바이저(메소드 + 어드바이스)
    private Map<String, Object> proxyMap = new HashMap<>(); //프록시 식별
    private Map<String ,Object> targetMap = new HashMap<>();
    private Map<String, List<String>> stand = new HashMap<>(); //키= 클래스명, 값 = 메소드들

    /**
     * 프록시 재생성
     * - 똑같은 자원이 들어오는 경우는, 이전 메소드에 이미 처리
     */
    public void reload_add_method(String className, List<String> roleNames) throws Exception {
        int flag =0;

        int lastIndex = className.lastIndexOf(".");
        Class type = getType(className, lastIndex);
        String methodName  = className.substring(lastIndex + 1); //메소드 명만
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1); //클래스 명만
        Object packageBean = ac.getBean(beanName); //빈


        List<ConfigAttribute> roles = new ArrayList<>();
        roleNames.forEach(r->roles.add(new SecurityConfig(r)));

        List<String> pull = stand.get(beanName);
        if(pull==null){
            List<String> pull_tmp = new ArrayList<>();
            pull_tmp.add(className);
            stand.put(beanName, pull_tmp);
        }
        else {
            pull.add(className);
            stand.put(beanName, pull);
        }

        ProxyFactory proxyFactory = advisorMap.get(beanName);
        if(proxyFactory == null) {
            flag=1;
            proxyFactory = new ProxyFactory();
        }
        proxyFactory.setTarget(packageBean);
        proxyFactory.addAdvice(customMethodSecurityInterceptor);
        advisorMap.put(beanName, proxyFactory);


        if(flag == 1){
            mapBasedMethodSecurityMetadataSource.addSecureMethod(type, methodName , roles);
            Object proxy = proxyFactory.getProxy();
            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry)applicationContext.getBeanFactory();
            registry.destroySingleton(beanName);
            registry.registerSingleton(beanName, proxy); //프록시 전환
        }
        else{
            mapBasedMethodSecurityMetadataSource.addSecureMethod(type, methodName , roles); //어드바이스 적용
        }
    }

    public void reload_delete_method(String className) throws Exception{
        int lastIndex = className.lastIndexOf(".");
        String tmp = className.substring(0, lastIndex);
        Class<?> type = ClassUtils.resolveClassName(tmp, ClassUtils.getDefaultClassLoader());
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
        Object packageBean = ac.getBean(beanName);

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry)applicationContext.getBeanFactory();
        ProxyFactory proxyFactory = advisorMap.get(beanName);
        proxyFactory.removeAdvice(customMethodSecurityInterceptor);


        registry.destroySingleton(beanName);
        registry.registerSingleton(beanName, packageBean);
    }

    private Class getType(String className, int lastIndex) {
        String typeName = className.substring(0, lastIndex);
        return ClassUtils.resolveClassName(typeName, ClassUtils.getDefaultClassLoader());
    }
}
