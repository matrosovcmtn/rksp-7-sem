package ru.matrosov.prac_04;

public class Book {
    private String title;
    private String author;

    public Book(String info) {
        String[] parts = info.split(" by ");
        this.title = parts[0];
        this.author = parts[1];
    }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}