package co.com.bancolombia.files;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.CustomIOException;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.uploadedfile.DataFile;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.utils.IoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.COPY_FILE_SUCCESS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_MOVING_FILE_IN_COPY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_MOVING_FILE_IN_REMOVE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_RETRIEVING_FILE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SUFFIX;

@Profile({"dev-local", "test"})
@Repository
public class DataFileRepositoryAdaper implements DataFileRepository {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.folderName}")
    private String folderName;

    private AmazonS3 s3;

    LoggerAdapter adapter = new LoggerAdapter(
            MY_APP, "SERVICE_DATAFILE_REPOSITORY", DataFileRepositoryAdapter.class.getName());

    public DataFileRepositoryAdaper() {
        s3 = AmazonS3ClientBuilder.standard().build();
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
            DeleteObjectRequest delete = new DeleteObjectRequest(bucketName, urlFile);
            s3.deleteObject(delete);
            return true;
        } catch (AmazonServiceException e) {
            adapter.error("ERROR AL ELIMINAR EL ARCHIVO EN EL BUCKET: " + urlFile, e);
            return false;
        }
    }

    @Override
    public InputStream retrieveFileInputStream(String url) {
        InputStream a = null;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, url);
            S3Object fileObject = s3.getObject(getObjectRequest);
            a = fileObject.getObjectContent();
        } catch (CustomException | SdkClientException e) {
            adapter.error(ERROR_RETRIEVING_FILE, e);
            return null;
        }
        return a;
    }

    @Override
    public String uploadS3(String destination, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, destination, file);
        PutObjectResult result = s3.putObject(putObjectRequest);
        return (null != result.getMetadata()) ? destination : null;
    }

    @Override
    public String generateUrl(String route, Date expiration) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, route)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
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
        byte[] bytes = IOUtils.toByteArray(input);
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
        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                    bucketName, sourceUrlKey, bucketName, destinationUrlKey);
            CopyObjectResult copyObjectResult = s3.copyObject(copyObjectRequest);
            adapter.info(COPY_FILE_SUCCESS + destinationUrlKey);
            adapter.info("ETag: " + copyObjectResult.getETag());
            adapter.info("LastModifiedDate: " + copyObjectResult.getLastModifiedDate());
            return true;
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            adapter.error("AmazonServiceException ERROR COPY FILE: " + sourceUrlKey, e);
            return false;
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            adapter.error("SdkClientException ERROR COPY FILE: " + sourceUrlKey, e);
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