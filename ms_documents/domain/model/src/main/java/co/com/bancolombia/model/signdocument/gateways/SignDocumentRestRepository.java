package co.com.bancolombia.model.signdocument.gateways;

import co.com.bancolombia.model.signdocument.SDRequestTotal;
import co.com.bancolombia.model.signdocument.SDResponseTotalWithLog;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface SignDocumentRestRepository {

    SDResponseTotalWithLog getSignature(SDRequestTotal requestTotal) throws MessagingException, IOException;
}
