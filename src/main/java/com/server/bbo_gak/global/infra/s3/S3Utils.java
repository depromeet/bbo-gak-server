package com.server.bbo_gak.global.infra.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Utils {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String getS3PresignedUrl(String fileName, HttpMethod httpMethod) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(httpMethod)
                .withExpiration(getPreSignedUrlExpiration());

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private Date getPreSignedUrlExpiration() {
        int PRESIGNED_EXPIRATION = 1000 * 60 * 30; //30분

        Date expiration = new Date();
        var expTimeMillis = expiration.getTime();
        expTimeMillis += PRESIGNED_EXPIRATION;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    public void deleteS3Object(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}
