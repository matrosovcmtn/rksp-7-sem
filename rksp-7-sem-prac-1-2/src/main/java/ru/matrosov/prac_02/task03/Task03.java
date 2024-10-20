package ru.matrosov.prac_02.task03;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//                    1 xor 1 = 0
//                    1 xor 0 = 1
//                    0 xor 1 = 1
//                    0 xor 0 = 0

public class Task03 {
    public static short calculateChecksum(String filePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             FileChannel fileChannel = fileInputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            short checksum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get(); //The ^= operator does an XOR
                }
                buffer.clear();
            }
            return checksum;
        }
    }

    public static void main(String[] args) {
        String filePath = "src/main/java/ru/matrosov/prac_02/task01/text.txt";
        try {
            short checksum = calculateChecksum(filePath);
            System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath, checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
