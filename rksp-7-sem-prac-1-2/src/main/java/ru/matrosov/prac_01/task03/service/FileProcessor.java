package ru.matrosov.prac_01.task03.service;

import ru.matrosov.prac_01.task03.model.File;

public abstract class FileProcessor {
    public void process(File file) {
        var delay = file.getSize() * 7;
        try {
            Thread.sleep(delay);
            System.out.printf("Усыпили поток [%s] при обработке на [%s] мс.%n", Thread.currentThread().getName(), delay);
            System.out.printf("Успешно обработали файл с типом [%s] и размером [%s].%n%n", file.getType(), file.getSize());
        } catch (InterruptedException ex) {
            System.out.println("Столкнулись с ошибкой при попытке усыпить поток. ");
        }
    }
}
