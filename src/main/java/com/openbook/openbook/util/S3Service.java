package com.openbook.openbook.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${aws.s3.bucket.name}")
    private String bucket;
    private final AmazonS3Client S3Client;
    private final int S3_PATH_LENGTH = 55;

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
            throw new OpenBookException(ErrorCode.FIlE_UPLOAD_FAILED);
        }
        return getFileUrlFromS3(fileName);
    }

    public void deleteFileFromS3(final String fileUrl){
        S3Client.deleteObject(bucket, fileUrl.substring(S3_PATH_LENGTH));
    }

    private String getFileUrlFromS3(final String fileName) {
        return S3Client.getUrl(bucket, fileName).toString();
    }

    private String getRandomFileName(final MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + file.getOriginalFilename();
    }

}
