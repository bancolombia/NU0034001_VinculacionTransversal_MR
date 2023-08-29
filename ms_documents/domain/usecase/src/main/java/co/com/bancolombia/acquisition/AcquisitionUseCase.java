package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessObjUploadDoc;

public interface AcquisitionUseCase {
    Acquisition getAcquisition(SqsMessObjUploadDoc obj);
}
