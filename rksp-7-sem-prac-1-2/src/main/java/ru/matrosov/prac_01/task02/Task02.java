package ru.matrosov.prac_01.task02;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Программа запрашивает у пользователя на вход число. Программа
 * имитирует обработку запроса пользователя в виде задержки от 1 до 5 секунд
 * выводит результат: число, возведенное в квадрат. В момент выполнения запроса
 * пользователь имеет возможность отправить новый запрос. Реализовать с
 * использованием Future
 */

public class Task02 {
    public static void main(String[] args) {
        var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        while (true) {
            System.out.print("Введите число (или 'exit' для выхода): ");
            var scanner = new Scanner(System.in);
            var userInput = scanner.nextLine();
            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }
            int number = Integer.parseInt(userInput);
            var future = executorService.submit(() -> calculateSquare(number));
            try {
                var result = (int) future.get();
                System.out.println("Результат: " + result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат числа. Пожалуйста, введите целое число.");
            }
        }
    }

    public static int calculateSquare(int number) {
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * number;
    }
}
