package de.larsgrefer;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.loader.jar.JarFile;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFilePerformanceTest {

    private File file;

    @Before
    public void setUp() {
        file = new File("../../primefaces-6.2.jar");
    }

    @Test
    public void testJava() throws IOException {
        ZipFile zipFile = new ZipFile(file);
        iterateAndMeasure(zipFile);
    }

    @Test
    public void testSpringBoot() throws IOException {
        ZipFile zipFile = new JarFile(file);
        iterateAndMeasure(zipFile);
    }

    private void iterateAndMeasure(ZipFile zipFile) {

        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            iterate(zipFile);
        }
        long end = System.nanoTime();
        double avgNs = (end - start) / 10d;

        System.out.printf("%f ms/iteration for %s", avgNs / 1000000, zipFile.getClass().getName());
    }

    private void iterate(ZipFile zipFile) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
        }
    }
}
