package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;

public interface UploadDocumentRutModifyCiiuUseCase {
    boolean modifyCiiu(String ciiu, Acquisition acquisition,
            String userTransaction, SqsMessageParamAllObject sqsMessageParamAllObject);
}
