package ru.matrosov.prac_01.task01.calculator;

public class SequentialSumCalculator {
    public long calculate(int[] array) throws InterruptedException {
        var sum = 0;
        for (int j : array) {
            sum += j;
            Thread.sleep(1);
        }
        return sum;
    }
}