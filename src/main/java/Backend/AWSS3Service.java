package Backend;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AWSS3Service {

    @Value("${myAccessKey}")
    private String accessKey;

    @Value("${mySecretKey}")
    private String secretKey;

    @Value("${myBucketName}")
    private String bucketName;

    public void uploadImagesToS3(List<MultipartFile> imageFiles) throws IOException {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        for (MultipartFile imageFile : imageFiles) {
            String uniqueImageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.getSize());

            String contentType = getContentTypeByFileExtension(Objects.requireNonNull(imageFile.getOriginalFilename()));
            metadata.setContentType(contentType);

            s3Client.putObject(bucketName, uniqueImageName, new ByteArrayInputStream(imageFile.getBytes()), metadata);

            String imageUrl = generateS3ImageUrl(uniqueImageName);

        }
    }

    private String generateS3ImageUrl(String imageName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + imageName;
    }

    private String getContentTypeByFileExtension(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }
}