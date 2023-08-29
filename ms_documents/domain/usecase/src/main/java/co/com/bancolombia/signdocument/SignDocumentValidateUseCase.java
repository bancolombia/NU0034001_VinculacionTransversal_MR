package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.signdocument.SDResponseError;

public interface SignDocumentValidateUseCase {

    void actionsErrors(Acquisition acquisition, SDResponseError responseError);
}
