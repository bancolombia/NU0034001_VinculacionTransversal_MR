package co.com.bancolombia.signdocument;

import co.com.bancolombia.model.signdocument.SDResponseOk;
import co.com.bancolombia.model.signdocument.SDResponseTotal;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZERO;

@RequiredArgsConstructor
public class SignDocumentResponseUseCaseImpl implements SignDocumentResponseUseCase {
    private final ObjectMapper objectMapper;

    @Override
    public SDResponseTotal createResponse(String rawResponse, String split) throws IOException, MessagingException {
        SDResponseOk sdResponseOk = objectMapper.readValue(messageConverter(splitMessage(rawResponse, split)),
                SDResponseOk.class);
        return SDResponseTotal.builder().responseOk(sdResponseOk).build();
    }

    public String splitMessage(String response, String split) {
        response = response.substring(response.indexOf(split) + split.length() + ONE);
        split = "--".concat(split).replaceAll("\"", "");
        response = response.substring(ZERO, response.indexOf(split)).trim();
        return response;
    }

    public String messageConverter(String rawMessage) throws IOException, MessagingException {
        Session session = Session.getDefaultInstance(new Properties());
        Message msg = new MimeMessage(session, new ByteArrayInputStream(rawMessage.getBytes()));
        InputStream inputStream = msg.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        int c;
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream,
                Charset.forName(StandardCharsets.UTF_8.name())))) {
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }
}