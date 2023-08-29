package co.com.bancolombia.model.uploaddocument;

import co.com.bancolombia.model.sqs.SqsMetaUploadDocument;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProcessDocument {
    private Integer userCode;
    private String processName;
    private String processCode;
    private String messageId;
    private List<ProcessDocumentFiles> files;
    private AcquisitionProcessDocument acquisition;
    private SqsMetaUploadDocument sqsMeta;
}
