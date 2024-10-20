package ru.matrosov.prac_01.task03.service;

import ru.matrosov.prac_01.task03.enumeration.FileType;
import ru.matrosov.prac_01.task03.model.File;

public interface FileGenerator {
    File generateFile(FileType type, int size);
}
