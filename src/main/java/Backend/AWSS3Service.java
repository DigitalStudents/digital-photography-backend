package Backend;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

    private static final Logger logger = LoggerFactory.getLogger(AWSS3Service.class);


    public List<String> uploadImagesToS3(List<MultipartFile> imageFiles) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();

            logger.info("Received {} images for processing.", imageFiles.size());

            for (MultipartFile imageFile : imageFiles) {
                try {
                    String uniqueImageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                    logger.info("Unique Image Name: {}", uniqueImageName);

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(imageFile.getSize());

                    String contentType = getContentTypeByFileExtension(Objects.requireNonNull(imageFile.getOriginalFilename()));
                    metadata.setContentType(contentType);

                    s3Client.putObject(bucketName, uniqueImageName, new ByteArrayInputStream(imageFile.getBytes()), metadata);

                    String imageUrl = generateS3ImageUrl(uniqueImageName);
                    imageUrls.add(imageUrl);

                    logger.info("Image uploaded successfully. URL: {}", imageUrl);
                } catch (Exception e) {
                    logger.error("Error uploading an image to S3: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Error uploading images to S3: {}", e.getMessage(), e);
            throw new IOException("Error uploading images to S3", e);
        }

        return imageUrls;
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