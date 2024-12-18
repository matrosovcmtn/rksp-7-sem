package ru.matrosov.rksp_7_sem_5_prac.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false)
    private byte[] content;

    @Column(nullable = false)
    private LocalDateTime uploadDateTime;

    @Column(nullable = false)
    private Long fileSize;

    @Column(length = 100)
    private String fileType;
}
