package com.bhq.ius.config.audit;

import com.bhq.ius.constant.IusConstant;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditProvider")
public class JpaAuditConfig {
    @Bean
    public AuditorAware<String> auditProvider() {
        String user = getUser();
        return () -> Optional.ofNullable(Optional.ofNullable(user)
                .orElse(ThreadContext.get(IusConstant.ACTION_USER) != null ? ThreadContext.get(IusConstant.ACTION_USER) : "UNKNOWS"));
    }

    private String getUser(){
        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails){
                return ((UserDetails) principal).getUsername();
            }else{
                return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
        }
        return null;
    }
}
