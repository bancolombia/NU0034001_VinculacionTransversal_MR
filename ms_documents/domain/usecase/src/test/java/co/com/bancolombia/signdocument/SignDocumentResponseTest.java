package co.com.bancolombia.signdocument;

import co.com.bancolombia.model.signdocument.SDResponseOk;
import co.com.bancolombia.model.signdocument.SDResponseTotal;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SignDocumentResponseTest {

    @InjectMocks
    @Spy
    SignDocumentResponseUseCaseImpl signDocumentResponseUseCase;

    @Mock
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createResponse() throws IOException, MessagingException {
        doReturn("asd").when(signDocumentResponseUseCase).messageConverter(anyString());
        doReturn("asd").when(signDocumentResponseUseCase).splitMessage(anyString(), anyString());
        SDResponseOk sdResponseOk = SDResponseOk.builder().build();
        doReturn(sdResponseOk).when(objectMapper).readValue(anyString(), eq(SDResponseOk.class));
        SDResponseTotal signDocument = signDocumentResponseUseCase.createResponse("asd", "-");
        assertNotNull(signDocument);
    }

    @Test
    public void splitMessage() {
        String asd = signDocumentResponseUseCase.splitMessage("--qwe asd --qwe", "qwe");
        assertNotNull(asd);
    }

    @Test
    public void messageConverter() throws IOException, MessagingException {
        String s = signDocumentResponseUseCase.messageConverter("asd");
        assertNotNull(s);
    }

}
