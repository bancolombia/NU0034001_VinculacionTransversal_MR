package co.com.bancolombia.model.sqs;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SqsMessDataObjUploadDoc {
	private SqsMessAcqObjUploadDoc acquisition;
	private List<SqsMessFileObjUploadDoc> files;
	private String messageId;
	private String processCode;
	private String processName;
	private String userCode;

}
