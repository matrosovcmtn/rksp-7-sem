package ru.matrosov.prac_02.task04;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Task04 {
    private Map<Path, String> fileContents = new HashMap<>();
    private Path directory;
    private WatchService watchService;

    public Task04() throws IOException {
        this.directory = Path.of("src/main/java/ru/matrosov");
        this.watchService = FileSystems.getDefault().newWatchService();
        this.directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var watcher = new Task04();
        watcher.startWatching();
    }

    public void startWatching() throws IOException, InterruptedException {
        while (true) {
            var key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                var fileName = (Path) event.context();
                var filePath = directory.resolve(fileName);
                if (fileName.toString().endsWith("~")) {
                    break;
                }

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("File created: " + fileName);
                    var content = Files.readString(filePath);
                    fileContents.put(filePath, content);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("File modified: " + fileName);
                    var newContent = Files.readString(filePath);
                    var oldContent = fileContents.get(filePath);
                    if (oldContent != null) {
                        printDifferences(oldContent, newContent);
                    }
                    fileContents.put(filePath, newContent);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("File deleted: " + fileName);
                    String oldContent = fileContents.remove(filePath);
                    if (oldContent != null) {
                        var contentBytes = oldContent.getBytes();
                        var fileSize = contentBytes.length;
                        var checksum = calculateChecksum(contentBytes);

                        System.out.println("Size: " + fileSize + " bytes");
                        System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath,
                                checksum);
                    }
                }
            }
            if (!key.reset()) {
                break;
            }
        }
    }

    private short calculateChecksum(byte[] contentBytes) throws IOException {
        Files.write(Path.of("temp.txt"), contentBytes);
        var f = new File("temp.txt");
        return calculateChecksum(f.getPath());
    }

    private void printDifferences(String oldContent, String newContent) {
        var oldLines = oldContent.split("\n");
        var newLines = newContent.split("\n");

        var oldSet = new HashSet<>(Arrays.asList(oldLines));
        var newSet = new HashSet<>(Arrays.asList(newLines));

        oldSet.removeAll(newSet); // строки, которые были удалены
        System.out.println("Deleted lines:");
        for (var line : oldSet) {
            System.out.println(line);
        }

        newSet.removeAll(new HashSet<>(Arrays.asList(oldLines))); // строки, которые были добавлены
        System.out.println("Added lines:");
        for (var line : newSet) {
            System.out.println(line);
        }
    }

    public static short calculateChecksum(String filePath) throws IOException {
        try (var fileInputStream = new FileInputStream(filePath);
             var fileChannel = fileInputStream.getChannel()) {
            var buffer = ByteBuffer.allocate(2);
            short checksum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get();
                }
                buffer.clear();
            }
            return checksum;
        }
    }
}
