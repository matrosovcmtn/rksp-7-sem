package ru.matrosov.prac_01.task01;

import ru.matrosov.prac_01.task01.calculator.ForkJoinSumCalculator;
import ru.matrosov.prac_01.task01.calculator.ParallelSumCalculator;
import ru.matrosov.prac_01.task01.calculator.SequentialSumCalculator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Номер в группе 25. Вариант 1. Поиск суммы элементов массива.
 */

public class Task01 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var array = new int[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        var sequentialSumCalculator = new SequentialSumCalculator();
        var parallelSumCalculator = new ParallelSumCalculator();
        var forkJoinSumCalculator = new ForkJoinSumCalculator(array, 0, array.length);

        // Последовательное выполнение
        var startTime = System.currentTimeMillis();
        var startMemory = getUsedMemory();
        var sum = sequentialSumCalculator.calculate(array);
        getStats(startTime, startMemory, sum, 1);


        // Параллельное выполнение
        startTime = System.currentTimeMillis();
        startMemory = getUsedMemory();
        var availableProcessors = Runtime.getRuntime().availableProcessors();
        sum = parallelSumCalculator.getSumInParallel(array, availableProcessors);
        getStats(startTime, startMemory, sum, availableProcessors);

        // С использованием ForkJoin
        startTime = System.currentTimeMillis();
        startMemory = getUsedMemory();
        var forkJoinPool = new ForkJoinPool();
        sum = forkJoinPool.invoke(forkJoinSumCalculator);
        getStats(startTime, startMemory, sum, forkJoinPool.getPoolSize());
    }

    private static void getStats(long startTime, long startMemory, long sum, int availableProcessors) {
        var usedMemory = (getUsedMemory() - startMemory) / 1024;
        var duration = (System.currentTimeMillis() - startTime);
        System.out.println("ForkJoin: " + duration + "мс " + "Потоков: " + availableProcessors);
        System.out.println("Сумма: " + sum);
        System.out.println("Использовано памяти: " + usedMemory + " KB");
    }

    public static long getUsedMemory() {
        var runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
