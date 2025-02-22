package com.example.support;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * <h1></h1>
 * Created by hanqf on 2022/5/8 08:24.
 */


public class AspectSupportUtils {
    private static ExpressionEvaluator evaluator = new ExpressionEvaluator();

    public static Object getKeyValue(JoinPoint joinPoint, String keyExpression) {
        if(keyExpression.contains("#") || keyExpression.contains("'")) {
            return getKeyValue(joinPoint.getTarget(), joinPoint.getArgs(), joinPoint.getTarget().getClass(),
                    ((MethodSignature) joinPoint.getSignature()).getMethod(), keyExpression);
        }
        return keyExpression;
    }

    private static Object getKeyValue(Object object, Object[] args, Class<?> clazz, Method method,
                                      String keyExpression) {
        if (StringUtils.hasText(keyExpression)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(object, clazz, method, args);
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
            return evaluator.key(keyExpression, methodKey, evaluationContext);
        }
        return SimpleKeyGenerator.generateKey(args);
    }

}
