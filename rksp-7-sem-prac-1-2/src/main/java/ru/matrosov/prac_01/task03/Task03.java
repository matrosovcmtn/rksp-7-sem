package ru.matrosov.prac_01.task03;

import ru.matrosov.prac_01.task03.enumeration.FileType;
import ru.matrosov.prac_01.task03.model.File;
import ru.matrosov.prac_01.task03.service.impl.FileGeneratorImpl;
import ru.matrosov.prac_01.task03.service.impl.JSONFileProcessor;
import ru.matrosov.prac_01.task03.service.impl.XLSFileProcessor;
import ru.matrosov.prac_01.task03.service.impl.XMLFileProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Task03 {
    public static void main(String[] args) {
        var typeList = List.of(FileType.XML, FileType.JSON, FileType.XLS);
        var fileGenerator = new FileGeneratorImpl();
        var typeToProcessor = Map.of(
                FileType.XML, new XMLFileProcessor(),
                FileType.JSON, new JSONFileProcessor(),
                FileType.XLS, new XLSFileProcessor()
        );
        var executors = Executors.newCachedThreadPool();
//        var executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        /*
          Ключевые особенности BlockingQueue:
          Блокирующие операции:
          Когда поток пытается добавить элемент в полную очередь, он блокируется до тех пор, пока не появится свободное место.
          Когда поток пытается извлечь элемент из пустой очереди, он блокируется до появления элемента.
         */
        var fileQueue = new ArrayBlockingQueue<File>(5);
        while (true) {
            getTreadsStates();
            // Генерируем файл с рандомным типом и размером
            var generatedFile = fileGenerator.generateFile(
                    typeList.get(ThreadLocalRandom.current().nextInt(0, 3)),
                    ThreadLocalRandom.current().nextInt(10, 100)
            );

            // Кладем файл в очередь
            try {
                fileQueue.put(generatedFile);
                System.out.printf("Добавили файл в очередь на обработку. Поток [%s]%n", Thread.currentThread().getName());
            } catch (InterruptedException ex) {
                System.out.printf("Поломались при попытке добавления файла в очередь. %s%n", ex.getMessage());
                break;
            }

            // Отправляем файл на обработку (берем из очереди. Если она пустая, то будем ждать)
            executors.submit(() -> {
                try {
                    System.out.println("Достаем из очереди и отправляем файл на обработку.\n");
                    var file = fileQueue.take();
                    typeToProcessor.get(file.getType()).process(file);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Поток был прерван при обработке файла");
                }
            });
            System.out.println("Текущая очередь: " + fileQueue + " Размер: " + fileQueue.size());
        }
        executors.close();
    }

    private static void getTreadsStates() {
        // Выводим состояние каждого потока
        System.out.println("\nСостояние потоков:");
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().startsWith("pool-")) {
                System.out.printf("Поток %s: %s%n", t.getName(), t.getState());
            }
        }
        System.out.println();
    }
}
