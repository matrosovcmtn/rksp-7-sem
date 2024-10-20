package ru.matrosov.prac_01.task03.model;

import ru.matrosov.prac_01.task03.enumeration.FileType;

public class File {
    private FileType type;
    private int size;

    public File(FileType type, int size) {
        this.type = type;
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public FileType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "File: [" + type.name() + "]; size: [" + size + "]";
    }
}
