package de.larsgrefer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

import java.util.Comparator;
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
        } finally {
            times.computeIfAbsent(proceedingJoinPoint.getSignature().toLongString(), k -> new LinkedList<>())
                    .add(System.nanoTime() - start);
        }
    }

    public static void printStats() {
        times.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(stringListEntry -> {
                    String signature = stringListEntry.getKey();
                    double avgMs = stringListEntry.getValue().stream().mapToLong(Long::longValue).average().orElse(0d) / 1000000d;
                    double sumMs = stringListEntry.getValue().stream().mapToLong(Long::longValue).sum() / 1000000d;
                    System.out.printf("%s -> %f ms total (%f ms avg)\n", signature, sumMs, avgMs);
                });
    }

}
