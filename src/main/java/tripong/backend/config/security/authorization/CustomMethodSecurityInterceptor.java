package tripong.backend.config.security.authorization;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

public class CustomMethodSecurityInterceptor extends AbstractSecurityInterceptor implements MethodInterceptor {

    private MethodSecurityMetadataSource securityMetadataSource;

    public void setSecurityMetadataSource(MethodSecurityMetadataSource metadataSource) {
        this.securityMetadataSource = metadataSource;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        InterceptorStatusToken token = super.beforeInvocation(invocation);

        Object result;
        try {
            result = invocation.proceed();
        }
        finally {
            super.finallyInvocation(token);
        }
        return super.afterInvocation(token, result);
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return MethodInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }
}
