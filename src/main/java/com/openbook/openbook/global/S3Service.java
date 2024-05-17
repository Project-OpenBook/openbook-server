package com.openbook.openbook.global;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.openbook.openbook.global.exception.OpenBookException;
import java.io.IOException;
import java.net.URL;
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

    public void uploadFileToS3(final MultipartFile file, final String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            S3Client.putObject(
                    new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
            );
        } catch (IOException e) {
            throw new OpenBookException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 에러");
        }
    }

    public void deleteFileFromS3(final String fileName){
        S3Client.deleteObject(bucket, fileName);
    }

    public URL getFileURLFromS3(final String fileName) {
        return S3Client.getUrl(bucket, fileName);
    }
}
