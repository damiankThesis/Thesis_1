package com.praca.komis.project.service;

import com.praca.komis.project.dto.UploadImageDTO;
import com.praca.komis.project.util.ChangeNameExistFileUtil;
import com.praca.komis.project.util.UploadImageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    @Value("${app.uploadImageDir}")
    private String uploadPath;

    public UploadImageDTO upload(MultipartFile multipartFile) {
        try( InputStream inputStream = multipartFile.getInputStream() ) {
            String savedFile = uploadImage(multipartFile.getOriginalFilename(), inputStream);
            return new UploadImageDTO(savedFile);
        } catch (IOException e){
            throw new RuntimeException("sth gone wrong\n", e);
        }
    }

    @SneakyThrows
    public String uploadImage(String filename, InputStream inputStream) {

        String newFileName = UploadImageUtils.slugifyFilename(filename);
        newFileName = ChangeNameExistFileUtil.renameIfExist(Path.of(uploadPath), newFileName);
        Path filePath = Paths.get(uploadPath).resolve(newFileName);

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            inputStream.transferTo(outputStream);       //input stream -> output (transfer bajtów)
        }
        return newFileName;
    }

    public Resource serveFiles(String filename) {
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        return fileSystemResourceLoader.getResource(uploadPath + filename);
    }
}
