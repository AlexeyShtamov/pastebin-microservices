package ru.shtamov.s3_service.application.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shtamov.s3_service.config.ObjectStorageConfig;
import ru.shtamov.s3_service.domain.Text;
import ru.shtamov.s3_service.extern.exceptions.TextIsNotFoundException;
import ru.shtamov.s3_service.extern.repositories.TextRepository;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TextService {

    private final ObjectStorageConfig objectStorageConfig;
    private final TextRepository textRepository;

    @Autowired
    public TextService(ObjectStorageConfig objectStorageConfig, TextRepository textRepository) {
        this.objectStorageConfig = objectStorageConfig;
        this.textRepository = textRepository;
    }

    /**
     * Метод для загрузки текста в object storage
     * @param text сущность текста
     * @return созданный текст
     */
    public Text upload(String text){

        final String bucketName = objectStorageConfig.getBucketName();
        final String accessKeyId = objectStorageConfig.getAccessKeyId();
        final String secretAccessKey = objectStorageConfig.getSecretAccessKey();
        final AmazonS3 s3Client = getAmazonS3(accessKeyId, secretAccessKey);
        String url;
        Text createdText;

        try {
            String fileName = generateUniqueName();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(text.length());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());

            s3Client.putObject(bucketName, fileName, inputStream, metadata);
            log.info("Upload Service. Added file: " + fileName + " to bucket: " + bucketName);

            url = s3Client.getUrl(bucketName, fileName).toExternalForm();
            Text createText = new Text();
            createText.setUuid(fileName);
            createText.setMetadata((long) text.length());
            createText.setBucketName(bucketName);
            createText.setUrl(url);
            createdText = textRepository.save(createText);

        } catch (AmazonS3Exception e) {
            throw new AmazonS3Exception(e.getMessage());
        }
        return createdText;

    }

    /**
     * Метод для получения текста из object storage по uuid
     * @param uuid итендификатор текста
     * @return текст в виде строки
     * @throws TextIsNotFoundException
     */
    public String get(String uuid) throws TextIsNotFoundException {
        Optional<Text> optionalText = textRepository.findById(uuid);
        if (optionalText.isPresent()){
            StringBuilder finalText = new StringBuilder();

            try (BufferedInputStream bis =
                         new BufferedInputStream(new URL(optionalText.get().getUrl()).openStream())) {
                int character;
                while ((character = bis.read()) != -1){
                    finalText.append((char) character);
                }
                log.info("File is read");
                return finalText.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new TextIsNotFoundException("Text with uuid " + uuid + " is not found");
    }

    /**
     * Метод для удаления текста по uuid
     * @param uuid итендификатор текста
     */
    public void delete(String uuid){
        final String bucketName = objectStorageConfig.getBucketName();
        final String accessKeyId = objectStorageConfig.getAccessKeyId();
        final String secretAccessKey = objectStorageConfig.getSecretAccessKey();
        final AmazonS3 s3Client;
        s3Client = getAmazonS3(accessKeyId, secretAccessKey);

        Optional<Text> optionalText = textRepository.findById(uuid);
        if (optionalText.isPresent()){
            Text text = optionalText.get();
            s3Client.deleteObject(bucketName, text.getUuid());
            textRepository.delete(text);
            log.info("Text with uuid {} is deleted", text.getUuid());
        }
    }

    /**
     * Метод для подключения к клиенту object storage
     * @param accessKeyId ключ доступа
     * @param secretAccessKey секрет ключа доступа
     * @return клинта s3
     */
    private static AmazonS3 getAmazonS3(String accessKeyId, String secretAccessKey) {
        final AmazonS3 s3Client;
        try {
            // Создание клиента AmazonS3 с подключением к Object Storage
            s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(
                                    "https://storage.yandexcloud.net",
                                    "ru-central1"
                            )
                    )
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                    .build();
        } catch (SdkClientException e) {
            throw new SdkClientException(e.getMessage());
        }
        return s3Client;
    }

    /**
     * Метод для генерации уникального uuid
     * @return uuid
     */
    private String generateUniqueName(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
