package co.com.bancolombia.model.uploaddocument.gateways;

import co.com.bancolombia.model.uploaddocument.ProcessDocumentKofaxRequest;
import co.com.bancolombia.model.uploaddocument.UploadDocumentWithLog;

import java.util.Date;

public interface ProcessDocumentRestRepository {
    UploadDocumentWithLog getRest(ProcessDocumentKofaxRequest processDocument, String messageId, Date dateRequestApi);
}
