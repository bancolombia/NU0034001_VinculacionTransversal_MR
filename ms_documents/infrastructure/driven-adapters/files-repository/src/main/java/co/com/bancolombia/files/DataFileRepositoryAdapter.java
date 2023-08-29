package co.com.bancolombia.files;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.CustomIOException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.uploadedfile.DataFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.IoUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COPY_FILE_SUCCESS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_MOVING_FILE_IN_COPY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_MOVING_FILE_IN_REMOVE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_RETRIEVING_FILE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SUFFIX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TIME_URL;

@Profile({"dev", "qa", "pdn"})
@Repository
public class DataFileRepositoryAdapter implements DataFileRepository {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.folderName}")
    private String folderName;

    private S3Client s3client;

    LoggerAdapter adapter = new LoggerAdapter(
            MY_APP, "SERVICE_DATAFILE_REPOSITORY", DataFileRepositoryAdapter.class.getName());

    public S3Client getS3client() {
        return s3client;
    }

    public void setS3client(S3Client s3client) {
        this.s3client = s3client;
    }

    @PostConstruct
    public void initializeAmazon() {
        this.setS3client(S3Client.builder().credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .region(Region.US_EAST_1).build());
    }

    @Override
    public DataFile save(DataFile managementFile) throws IOException {
        String destination = this.folderName + '/' + managementFile.getFolder() + '/' + managementFile.getName();
        managementFile.setUrl(this.uploadS3(destination, managementFile.getFile()));
        try {
            Files.deleteIfExists(managementFile.getFile().toPath());
        } catch (CustomIOException e) {
            String msError = "ERROR AL GRABAR EL ARCHIVO EN EL BUCKET";
            adapter.error(msError, e);
        }
        return managementFile;
    }

    @Override
    public boolean remove(String urlFile) {
        try {
            DeleteObjectRequest delete = DeleteObjectRequest.builder().bucket(bucketName).key(urlFile).build();
            s3client.deleteObject(delete);
            return true;
        } catch (SdkClientException e) {
            adapter.error("ERROR AL ELIMINAR EL ARCHIVO EN EL BUCKET", e);
            return false;
        }
    }

    @Override
    public InputStream retrieveFileInputStream(String url) {
        InputStream a = null;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(url).build();
            a = s3client.getObject(getObjectRequest);
        } catch (CustomException | S3Exception e) {
            adapter.error(ERROR_RETRIEVING_FILE, e);
            return null;
        }
        return a;
    }

    @Override
    public String uploadS3(String destination, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(destination).build();
        PutObjectResponse result = s3client.putObject(putObjectRequest, RequestBody.fromFile(file));
        return (null != result.responseMetadata()) ? destination : null;
    }

    @Override
    public String generateUrl(String route, Date expiration) {
        S3Presigner presigner = S3Presigner.builder().credentialsProvider(WebIdentityTokenFileCredentialsProvider
                .create()).region(Region.US_EAST_1).build();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(route).build();
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(TIME_URL)).getObjectRequest(getObjectRequest).build();
        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        presigner.close();
        URL url = presignedGetObjectRequest.url();
        return url.toString();
    }

    @Override
    public String getBase64File(String url) throws IOException {
        InputStream inputStream = this.retrieveFileInputStream(url);
        if (inputStream != null) {
            return displayTextInputStream(inputStream);
        } else {
            throw new IOException();
        }
    }

    @Override
    public String getBase64File(String fileName, String folderNameDirectory) throws IOException {
        String urlTotal = folderName + SUFFIX + folderNameDirectory + SUFFIX + fileName;
        InputStream inputStream = this.retrieveFileInputStream(urlTotal);
        if (inputStream != null) {
            return displayTextInputStream(inputStream);
        } else {
            throw new IOException();
        }
    }

    public String displayTextInputStream(InputStream input) throws IOException {
        byte[] bytes = IoUtils.toByteArray(input);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public InputStream getInputStreamOfFile(String fileName, String folderNameDirectory) {
        String urlTotal = folderName + SUFFIX + folderNameDirectory + SUFFIX + fileName;
        return this.retrieveFileInputStream(urlTotal);
    }

    @Override
    public File convertInputStreamToFile(InputStream inputStream, File file) {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public boolean copyBucketObject(String sourceUrlKey, String destinationUrlKey) {
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(bucketName + "/" + sourceUrlKey, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            adapter.error("URL could not be encoded: " + e.getMessage());
        }

        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .copySource(encodedUrl)
                .destinationBucket(bucketName)
                .destinationKey(destinationUrlKey)
                .build();
        try {
            CopyObjectResponse copyObjectResponse = s3client.copyObject(copyObjectRequest);
            adapter.info(
                    COPY_FILE_SUCCESS + destinationUrlKey + SPACE + copyObjectResponse.copyObjectResult().toString());
            return true;
        } catch (S3Exception e) {
            adapter.error(
                    "ERROR COPY FILE S3Exception: " + sourceUrlKey + SPACE + e.awsErrorDetails().errorMessage(), e);
            return false;
        }
    }

    @Override
    public boolean moveBucketObject(String sourceUrlKey, String destinationUrlKey) {
        if (copyBucketObject(sourceUrlKey, destinationUrlKey)) {
            if (remove(sourceUrlKey)) {
                return true;
            } else {
                adapter.error(ERROR_MOVING_FILE_IN_REMOVE + sourceUrlKey);
                return false;
            }
        }
        adapter.error(ERROR_MOVING_FILE_IN_COPY + sourceUrlKey);
        return false;
    }

    @Override
    public byte[] getBase64Bytes(String fileName, String folderNameDirectory) throws IOException {
        String urlTotal = folderName + SUFFIX + folderNameDirectory + SUFFIX + fileName;
        InputStream inputStream = this.retrieveFileInputStream(urlTotal);
        if (inputStream != null) {
            return getBase64Bytes(inputStream);
        } else {
            throw new IOException();
        }
    }

    private byte[] getBase64Bytes(InputStream input) throws IOException {
        return IoUtils.toByteArray(input);
    }
}