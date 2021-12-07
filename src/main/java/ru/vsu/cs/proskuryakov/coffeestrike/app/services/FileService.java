package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.FileNotLoadedException;

import java.util.Objects;

@Service
public class FileService {

    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.url}")
    private String url;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public FileService(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadFile(MultipartFile file, String fileName) throws FileNotLoadedException {
        if (!Objects.equals(file.getContentType(), "image/jpeg"))
            throw new FileNotLoadedException("Не подходящий тип файла");
        try {
            PutObjectRequest req = new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata());
            s3client.putObject(req.withCannedAcl(CannedAccessControlList.PublicRead));
            return url + fileName;
        } catch (Exception ex) {
            throw new FileNotLoadedException(ex.getMessage());
        }
    }

}
