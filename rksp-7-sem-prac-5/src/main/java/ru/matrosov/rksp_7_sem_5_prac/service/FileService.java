package ru.matrosov.rksp_7_sem_5_prac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.matrosov.rksp_7_sem_5_prac.model.File;
import ru.matrosov.rksp_7_sem_5_prac.repository.FileRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public File uploadFile(MultipartFile multipartFile) throws IOException {
        if (fileRepository.existsByFileName(multipartFile.getOriginalFilename())) {
            throw new RuntimeException("File already exists");
        }

        var file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setContent(multipartFile.getBytes());
        file.setUploadDateTime(LocalDateTime.now());
        file.setFileSize(multipartFile.getSize());
        file.setFileType(multipartFile.getContentType());

        return fileRepository.save(file);
    }

    @Transactional(readOnly = true)
    public File downloadFile(String fileName) {
        var file = fileRepository.findByFileName(fileName);
        if (file == null) {
            throw new RuntimeException("File not found");
        }
        return file;
    }
}