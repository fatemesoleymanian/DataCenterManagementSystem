package com.example.DataCenterManagementSystem.config.logging;


import com.example.DataCenterManagementSystem.entity.security.ActivityLog;
import com.example.DataCenterManagementSystem.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogRepository logRepository;

    @AfterReturning(pointcut = "@annotation(logActivity)", returning = "result")
    public void log(JoinPoint joinPoint, LogActivity logActivity, Object result) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        ActivityLog log = new ActivityLog();
        log.setUsername(username);
        log.setAction(logActivity.action());

        logRepository.save(log);
    }
}
