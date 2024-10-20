package ru.matrosov.prac_03.task04;

import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;

import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class FileProcessor {

    private final FileType fileType;
    private final BlockingQueue<File> fileQueue;

    public Observable<File> processFiles() {
        return Observable.create(emitter -> {
            while (!emitter.isDisposed()) {
                try {
                    File file = fileQueue.take();
                    if (file.getFileType().equals(fileType)) {
                        Thread.sleep(file.getFileSize() * 7L);
                        emitter.onNext(file);
                        System.out.println("Processed file: " + file.getFileType() + " " + Thread.currentThread());
                    }
                } catch (InterruptedException e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
