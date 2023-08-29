package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.SignDocument;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface SignDocumentUseCase {

    SignDocument startProcess(Acquisition acquisition, SDRequestTxt sdRequestTxt) throws
            IOException, MessagingException;
}
