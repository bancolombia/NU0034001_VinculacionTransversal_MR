package co.com.bancolombia.model.uploadedfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest {
	private Long id;
	private Long customer;       
	private Long typeFile;   
	private String file;
	private String fileName;
	private String mimeType;
}
