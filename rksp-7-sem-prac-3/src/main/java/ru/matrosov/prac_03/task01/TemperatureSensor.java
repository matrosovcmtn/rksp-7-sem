package ru.matrosov.prac_03.task01;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class TemperatureSensor {
    private final PublishSubject<Double> temperatureSubject = PublishSubject.create();
    private final Random random = new Random();

    public TemperatureSensor() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(tick -> {
                    double temperature = 15 + random.nextDouble() * 15;
                    temperatureSubject.onNext(temperature);
                });
    }
}
