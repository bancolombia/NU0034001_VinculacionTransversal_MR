package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.generatetoken.GenerateToken;

import java.text.ParseException;

public interface GenerateTokenUseCase {
    GenerateToken save (GenerateToken generateToken, InfoReuseCommon infoReuseCommon);
    GenerateToken startProcessGenerateToken
            (BasicAcquisitionRequest basicAcquisitionRequest, GenerateToken generateToken) throws ParseException;
    String getCellphoneByLastToken(Acquisition acquisition);
}
