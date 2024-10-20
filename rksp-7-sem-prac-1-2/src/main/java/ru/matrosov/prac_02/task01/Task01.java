package ru.matrosov.prac_02.task01;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Task01 {
    public static void main(String[] args) {
        var filePath = Paths.get("src/main/java/ru/matrosov/prac_02/task01/text.txt");

        try (WritableByteChannel channel = Channels.newChannel(System.out)) {
            var fileContent = Files.readAllLines(filePath);
            System.out.println("Содержимое файла:");
            for (var line : fileContent) {
                var buffer = ByteBuffer.wrap((line + "\n").getBytes(StandardCharsets.UTF_8));
                channel.write(buffer);
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
