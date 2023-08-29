package co.com.bancolombia.signdocument;

import co.com.bancolombia.model.signdocument.SDResponseTotal;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface SignDocumentResponseUseCase {

    SDResponseTotal createResponse(String response, String rawResponse) throws IOException, MessagingException;
}
