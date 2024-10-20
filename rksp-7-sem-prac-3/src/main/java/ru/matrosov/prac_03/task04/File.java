package ru.matrosov.prac_03.task04;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class File {
    private final FileType fileType;
    private final int fileSize;
}