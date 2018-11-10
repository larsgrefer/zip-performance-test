package de.larsgrefer.example;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ZipFileScanTest {

    @Test
    public void classpathScan() {

        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            try (ScanResult scanResult = new ClassGraph()
                    .enableClassInfo()
                    .enableAnnotationInfo()
                    .scan()) {
                assertFalse(scanResult.getAllClasses().isEmpty());
            }
        }
        long end = System.nanoTime();

        double avgNs = (end - start) / 10d;

        System.out.printf("Classpath scan took %f ms\n", avgNs / 1000000d);
    }
}
