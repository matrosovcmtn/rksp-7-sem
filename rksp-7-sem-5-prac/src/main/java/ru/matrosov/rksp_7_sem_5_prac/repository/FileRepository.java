package ru.matrosov.rksp_7_sem_5_prac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matrosov.rksp_7_sem_5_prac.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    boolean existsByFileName(String fileName);
    File findByFileName(String fileName);
}