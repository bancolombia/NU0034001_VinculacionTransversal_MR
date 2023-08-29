package co.com.bancolombia.validatedataextraction;

import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.logfunctionalvnt.log.model.LogFunctionalReuse;
import co.com.bancolombia.model.asyncdigitalization.AsyncDigitalization;
import co.com.bancolombia.model.validatedataextraction.UploadDocumentApiResponseData;
import co.com.bancolombia.model.validatedataextraction.ValidateDataExtraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import static co.com.bancolombia.commonsvnt.util.ConstantLog.LOGFIELD_REUSE_INFO;


public class ValidateDataExtractionLogUseCaseImpl implements ValidateDataExtractionLogUseCase{

    public ValidateDataExtractionLogUseCaseImpl() {
        //Construct empty by the moment
    }
    @Autowired
    private WebRequest webRequest;

    @Override
	public void saveInfoLog(AsyncDigitalization asyncDigitalization) {
        LogFunctionalReuse logFunctionalReuse = LogFunctionalReuse.builder()
                .requestReuse(asyncDigitalization.getRequestReuse())
                .responseReuse(asyncDigitalization.getResponseReuse())
                .dateRequest(asyncDigitalization.getRequestDateReuse())
                .dateResponse(asyncDigitalization.getResponseDateReuse())
                .build();

        webRequest.setAttribute(LOGFIELD_REUSE_INFO, logFunctionalReuse, RequestAttributes.SCOPE_SESSION);
	}

    public ValidateDataExtraction getObjectValid(AsyncDigitalization asyncDigitalization,
                                                 UploadDocumentApiResponseData objSuccess) {

        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder()
                .dateRequestReuse(asyncDigitalization.getRequestDateReuse())
                .dateResponseReuse(asyncDigitalization.getResponseDateReuse())
                .requestReuse(asyncDigitalization.getRequestReuse())
                .responseReuse(asyncDigitalization.getResponseReuse())
                .build();

        return ValidateDataExtraction.builder().uploadDocumentApiResponseData(objSuccess)
                .infoReuseCommon(infoReuseCommon)
                .build();
    }

}
