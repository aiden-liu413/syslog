package com.cloudwise.syslog4j.condition;

import com.cloudwise.syslog4j.annotation.EnableSyslog;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class OnAnnotationCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> beansWithAnnotation = context.getBeanFactory().getBeansWithAnnotation(EnableSyslog.class);
        return new ConditionOutcome(!beansWithAnnotation.isEmpty(), "skipped the configure of syslog");
    }
}
