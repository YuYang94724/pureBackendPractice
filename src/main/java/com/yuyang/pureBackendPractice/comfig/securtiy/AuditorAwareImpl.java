package com.yuyang.pureBackendPractice.comfig.securtiy;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    //用來讓memberPO可以獲取使用者`createdBy` 和 `lastModifiedBy` 字段的值
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(v -> (AbstractAuthenticationToken) v)
                .map(AbstractAuthenticationToken::getName)
                .or(() -> Optional.of(""));
    }
}
