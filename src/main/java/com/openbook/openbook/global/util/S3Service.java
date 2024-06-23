package com.openbook.openbook.global.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.openbook.openbook.global.exception.OpenBookException;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${aws.s3.bucket.name}")
    private String bucket;
    private final AmazonS3Client S3Client;

    public String uploadFileAndGetUrl(final MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        String fileName = getRandomFileName(file);
        try {
            S3Client.putObject(
                    new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
            );
        } catch (IOException e) {
            throw new OpenBookException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 에러");
        }
        return getFileUrlFromS3(fileName);
    }

    public void deleteFileFromS3(final String fileName){
        S3Client.deleteObject(bucket, fileName);
    }

    private String getFileUrlFromS3(final String fileName) {
        return S3Client.getUrl(bucket, fileName).toString();
    }

    private String getRandomFileName(final MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + file.getOriginalFilename();
    }

}
