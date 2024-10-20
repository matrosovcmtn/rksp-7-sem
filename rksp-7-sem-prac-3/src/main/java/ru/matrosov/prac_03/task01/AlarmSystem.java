package ru.matrosov.prac_03.task01;

import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmSystem {
    private static final double MAX_TEMPERATURE = 25.0;
    private static final int MAX_CO2 = 70;

    private final TemperatureSensor temperatureSensor;
    private final CO2Sensor co2Sensor;

    /**
     * combineLatest комбинирует несколько Observable потоков;
     * берет последнее значение из каждого потока и комбинирует их
     * вместе каждый раз, когда любой из потоков выдает новое значение
     */
    public void init() {
        Observable.combineLatest(
                temperatureSensor.getTemperatureSubject(),
                co2Sensor.getCo2Subject(),
                this::checkAlarmCondition
        ).subscribe(
                this::handleAlarm,
                this::handleError,
                () -> System.out.println("Monitoring completed"));
    }

    private String checkAlarmCondition(Double temperature, Integer co2Level) {
        boolean tempExceeded = temperature > MAX_TEMPERATURE;
        boolean co2Exceeded = co2Level > MAX_CO2;

        if (tempExceeded && co2Exceeded) {
            return "ALARM!!!";
        } else if (tempExceeded) {
            return "Warning: Temperature exceeded!" + temperature;
        } else if (co2Exceeded) {
            return "Warning: CO2 level exceeded!" + co2Level;
        }
        return "Normal";
    }

    private void handleAlarm(String alarmMessage) {
        if (alarmMessage != null) {
            System.out.println(alarmMessage);
        }
    }

    private void handleError(Throwable error) {
        System.out.println("An error occurred in the monitoring system" + error);
    }
}
