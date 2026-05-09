package com.javaeasybank.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 檔案上傳服務 — 將圖片存至本地檔案系統，回傳可存取的 URL 路徑。
 */
@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(
            @Value("${app.upload.dir:uploads}") String uploadPath) {
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("無法建立上傳目錄: " + this.uploadDir, e);
        }
    }

    /**
     * 儲存檔案，回傳相對路徑（如 /uploads/abc123.jpg）
     *
     * @param file     上傳的檔案
     * @param subDir   子目錄（如 "id-cards"）
     * @return 檔案的 URL 路徑
     */
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上傳檔案不可為空");
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        // 只允許圖片格式
        String lower = ext.toLowerCase();
        if (!lower.equals(".jpg") && !lower.equals(".jpeg") && !lower.equals(".png")) {
            throw new IllegalArgumentException("僅支援 JPG / PNG 格式");
        }

        // 檔案大小限制 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("檔案大小不可超過 5MB");
        }

        String fileName = UUID.randomUUID() + ext;
        Path targetDir = uploadDir.resolve(subDir);

        try {
            Files.createDirectories(targetDir);
            Path targetPath = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + subDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("檔案儲存失敗", e);
        }
    }
}
