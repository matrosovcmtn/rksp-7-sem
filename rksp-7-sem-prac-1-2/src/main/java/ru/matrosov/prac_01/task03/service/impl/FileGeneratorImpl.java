package ru.matrosov.prac_01.task03.service.impl;

import ru.matrosov.prac_01.task03.enumeration.FileType;
import ru.matrosov.prac_01.task03.model.File;
import ru.matrosov.prac_01.task03.service.FileGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class FileGeneratorImpl implements FileGenerator {
    @Override
    public File generateFile(FileType type, int size) {
        var randomDelay = ThreadLocalRandom.current().nextInt(100, 1000);
        try {
            Thread.sleep(randomDelay);
            System.out.printf("Усыпили поток [%s] при генерации на [%s] мс. %n", Thread.currentThread().getName(), randomDelay);
            System.out.printf("Создали файл с типом [%s] и размером [%s].%n", type, size);
            return new File(type, size);
        } catch (InterruptedException ex) {
            System.out.println("Столкнулись с ошибкой при попытке усыпить поток. ");
            return null;
        }
    }
}
