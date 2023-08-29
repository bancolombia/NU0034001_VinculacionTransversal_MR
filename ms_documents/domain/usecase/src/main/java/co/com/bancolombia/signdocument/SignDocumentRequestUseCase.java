package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.signdocument.SDRequest;
import co.com.bancolombia.model.signdocument.SDRequestTxt;

import java.io.IOException;

public interface SignDocumentRequestUseCase {

    SDRequest createRequest(Acquisition acquisition, SDRequestTxt requestTxt) throws IOException;
}
