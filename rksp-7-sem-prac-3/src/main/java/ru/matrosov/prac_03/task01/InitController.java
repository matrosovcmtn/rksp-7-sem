package ru.matrosov.prac_03.task01;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitController {
    private final AlarmSystem alarmSystem;

    @GetMapping("/init")
    public void init() {
        alarmSystem.init();
    }
}
