package tripong.backend.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.role.Resource;
import tripong.backend.repository.admin.resource.ResourceRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getUrlRequestMap(){
        List<Resource> resources = resourceRepository.findUrlAllResources();
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> beforeRequestMap = new LinkedHashMap<>();
        resources.forEach( r->{
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            r.getRoleResources().forEach( role ->{
                configAttributeList.add(new SecurityConfig(role.getRole().getRoleName()));
            });
            beforeRequestMap.put(new AntPathRequestMatcher(r.getResourceName()), configAttributeList);
        });
        return beforeRequestMap;
    }

}
