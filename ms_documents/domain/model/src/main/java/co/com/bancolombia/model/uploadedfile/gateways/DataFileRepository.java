package co.com.bancolombia.model.uploadedfile.gateways;

import co.com.bancolombia.model.uploadedfile.DataFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public interface DataFileRepository {
	public DataFile save(DataFile managementFile) throws IOException;

	public boolean remove(String urlFile);

	public InputStream retrieveFileInputStream(String url) throws IOException;

	String uploadS3(String destination, File file);

	public String generateUrl(String route, Date expiration);

	public String getBase64File(String url) throws IOException;

	public String getBase64File(String fileName, String folderNameDirectory) throws IOException;

	public String displayTextInputStream(InputStream input) throws IOException;

	InputStream getInputStreamOfFile(String fileName, String folderNameDirectory);

	File convertInputStreamToFile(InputStream inputStream, File file);

	boolean copyBucketObject(String sourceUrlKey, String destinationUrlKey);

	boolean moveBucketObject(String sourceUrlKey, String destinationUrlKey);

	byte[] getBase64Bytes(String fileName, String folderNameDirectory) throws IOException;
}
