package de.larsgrefer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
public class PerformanceAspect {

    public static Map<String, List<Long>> times = new ConcurrentHashMap<>();

    //@Around("execution(* org.springframework.boot.loader.jar.*.**(..))")
    public Object measure(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            times.computeIfAbsent(proceedingJoinPoint.getSignature().toLongString(), k -> new LinkedList<>())
                    .add(System.nanoTime() - start);
        }
    }

}
