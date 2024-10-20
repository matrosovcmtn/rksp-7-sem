package ru.matrosov.prac_02.task02;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Task02 {

    private static long memoryUsed = 0L;

    public static void main(String[] args) {
        var sourceFile = "source.txt";
        var destinationFile1 = "destination1.txt";
        var destinationFile2 = "destination2.txt";
        var destinationFile3 = "destination3.txt";
        var destinationFile4 = "destination4.txt";

        try {
            createLargeFile(sourceFile);
            runCopyMethod("FileInputStream/FileOutputStream", () -> copyUsingFileStreams(sourceFile, destinationFile1));
            runCopyMethod("FileChannel", () -> copyUsingFileChannel(sourceFile, destinationFile2));
            runCopyMethod("Apache Commons IO", () -> copyUsingApacheCommonsIO(sourceFile, destinationFile3));
            runCopyMethod("Files class", () -> copyUsingFilesClass(sourceFile, destinationFile4));

        } catch (IOException e) {
            System.err.println("Произошла ошибка при выполнении операций: " + e.getMessage());
        }
    }

    private static void runCopyMethod(String methodName, CopyMethod method) {
        var startTime = System.currentTimeMillis();
        try {
            method.copy();
            getStats(methodName, startTime);
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении метода " + methodName + ": " + e.getMessage());
        }
    }

    private static void createLargeFile(String fileName) throws IOException {
        var data = new byte[1024 * 1024];
        try (var fos = new FileOutputStream(fileName)) {
            for (int i = 0; i < 100; i++) {
                fos.write(data);
            }
        }
    }

    private static void copyUsingFileStreams(String source, String destination) throws IOException {
        try (var fis = new FileInputStream(source);
             var fos = new FileOutputStream(destination)) {
            var buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void copyUsingFileChannel(String source, String destination) throws IOException {
        try (var fis = new FileInputStream(source);
             var fos = new FileOutputStream(destination);
             var sourceChannel = fis.getChannel();
             var destinationChannel = fos.getChannel()) {
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        }
    }

    private static void copyUsingApacheCommonsIO(String source, String destination) throws IOException {
        var sourceFile = new File(source);
        var destFile = new File(destination);
        FileUtils.copyFile(sourceFile, destFile);
    }

    private static void copyUsingFilesClass(String source, String destination) throws IOException {
        var sourcePath = Path.of(source);
        var destinationPath = Path.of(destination);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void getStats(String method, long startTime) {
        var endTime = System.currentTimeMillis();
        var elapsedTime = endTime - startTime;
        System.out.println("Метод " + method + ":");
        System.out.println("Время выполнения: " + elapsedTime + " мс");
        var runtime = Runtime.getRuntime();
        long memoryOperationUsed = memoryUsed == 0 || runtime.freeMemory() > memoryUsed
                ? runtime.totalMemory() - runtime.freeMemory()
                : memoryUsed - runtime.freeMemory();
        memoryUsed = runtime.freeMemory();
        System.out.println("Использование памяти: " + memoryOperationUsed / (1024) + "  КБ");
        runtime.gc();
    }

    @FunctionalInterface
    interface CopyMethod {
        void copy() throws IOException;
    }
}