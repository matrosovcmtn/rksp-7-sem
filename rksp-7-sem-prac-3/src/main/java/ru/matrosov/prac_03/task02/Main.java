package ru.matrosov.prac_03.task02;

import io.reactivex.rxjava3.core.Observable;
import lombok.var;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //Преобразовать поток из 1000 случайных чисел от 0 до 1000 в поток,
        //содержащий только числа больше 500
        Random random = new Random();
        Observable
                .range(0, 1000)
                .filter(number -> number > 500)
                .subscribe(number -> System.out.print(number + " "));

        System.out.println();

        //Даны два потока по 1000 элементов. Каждый содержит случайные
        //цифры. Сформировать поток, обрабатывающий оба потока последовательно.
        //Например, при входных потоках (1, 2, 3) и (4, 5, 6) выходной поток — (1, 2, 3, 4,
        //5, 6).
        Observable<Integer> numbers1 = Observable.range(0, 100).map(i -> random.nextInt(10));
        Observable<Integer> numbers2 = Observable.range(0, 100).map(i -> random.nextInt(10));
        Observable.concat(numbers1, numbers2).subscribe(s -> System.out.print(s + " "));

        System.out.println();

        //Дан поток из 10 случайных чисел. Сформировать поток, содержащий
        //только первые 5 чисел.
        var rand1 = Observable.range(0, 10).map(i -> random.nextInt(10));

        rand1.take(5)
                .subscribe(System.out::print);
    }
}
