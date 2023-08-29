package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;

import java.text.ParseException;

public interface GenerateTokenConstructUseCase {

    GTRequest createRequestGetToken(GenerateToken generateToken, String documentNumber, String documentType);

    GenerateToken reFormatGenerateToken(GenerateToken generateToken, BasicAcquisitionRequest request,
            GTResponseWithLog generateTokenResponseWithLog) throws ParseException;
}
