package com.iCommerce.product.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (Objects.nonNull(context.getAuthentication())) {
            return Optional.of(context.getAuthentication().getName());
        }
        return Optional.of("system");
    }
}
