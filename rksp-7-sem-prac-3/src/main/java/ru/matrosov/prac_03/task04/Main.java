package ru.matrosov.prac_03.task04;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(5);
        FileGenerator fileGenerator = new FileGenerator(queue);

        List<FileProcessor> processors = Arrays.stream(FileType.values())
                .map(type ->
                        new FileProcessor(type, queue)
                )
                .collect(Collectors.toList());

        List<Observable<File>> observables = processors.stream()
                .map(p -> {
                    return p.processFiles()
                            .subscribeOn(Schedulers.io());
                })
                .collect(Collectors.toList());

        Observable<File> FileObservable3 = fileGenerator.run().subscribeOn(
                Schedulers.io()
        );

        FileObservable3.subscribe(queue::add);
        observables.forEach(Observable::subscribe);

        // Ожидаем завершения всех задач
        System.out.println("Все задачи завершены.");

        Thread.sleep(100000000000L);
    }
}
