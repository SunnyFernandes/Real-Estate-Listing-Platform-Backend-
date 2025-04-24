package com.relp.service.AWS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.relp.payload.property.PropertyDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class BucketService {

    private AmazonS3 amazonS3;

    public BucketService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public String uploadfile(
            String bucketName,
            PropertyDto propertyById,
            MultipartFile file
    )throws IOException {

        String uniqueFileName = "Property/" + propertyById + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest request = new PutObjectRequest(bucketName,uniqueFileName,file.getInputStream(),metadata);

        amazonS3.putObject(request);

        return amazonS3.getUrl(bucketName,uniqueFileName).toString();
    }
}
