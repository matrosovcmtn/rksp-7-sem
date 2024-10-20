package ru.matrosov.prac_01.task01.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelSumCalculator {

    public long getSumInParallel(int[] array, int numThreads) throws ExecutionException, InterruptedException {
        int len = array.length;
        int partSize = len / numThreads;
        var executor = Executors.newCachedThreadPool();
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int start = i * partSize;
            int end = (i == numThreads - 1) ? len : (i + 1) * partSize;
            var task = new ParallelSumTask(array, start, end);
            futures.add(executor.submit(task));
        }
        int totalSum = 0;
        for (var future : futures) {
            totalSum += future.get();
        }
        executor.shutdown();
        return totalSum;
    }

    private record ParallelSumTask(int[] array, int start, int end) implements Callable<Integer> {
        @Override
        public Integer call() throws InterruptedException {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
                Thread.sleep(1);
            }
            return sum;
        }
    }
}
