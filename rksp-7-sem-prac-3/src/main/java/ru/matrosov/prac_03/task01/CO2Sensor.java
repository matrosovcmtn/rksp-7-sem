package ru.matrosov.prac_03.task01;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class CO2Sensor {
    private final PublishSubject<Integer> co2Subject = PublishSubject.create();
    private final Random random = new Random();

    public CO2Sensor() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(tick -> {
                    int co2Level = 30 + random.nextInt(71);
                    co2Subject.onNext(co2Level);
                });
    }
}
