package co.com.bancolombia.signdocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequest;
import co.com.bancolombia.model.signdocument.SDRequestTotal;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.SDResponseDatum;
import co.com.bancolombia.model.signdocument.SDResponseOk;
import co.com.bancolombia.model.signdocument.SDResponseTotal;
import co.com.bancolombia.model.signdocument.SDResponseTotalWithLog;
import co.com.bancolombia.model.signdocument.SignDocument;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRepository;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRestRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SIGNDOCUMENT;

@RequiredArgsConstructor
public class SignDocumentUseCaseImpl implements SignDocumentUseCase {

    private final SignDocumentValidateUseCase signDocumentUseCase;
    private final SignDocumentRequestUseCase signDocumentRequestUseCase;
    private final SignDocumentRepository repository;
    private final CoreFunctionDate coreFunctionDate;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final SignDocumentRestRepository signDocumentRestRepository;

    public SignDocument save(SignDocument signDocument) {
        return repository.save(signDocument);
    }

    @Override
    public SignDocument startProcess(Acquisition acquisition, SDRequestTxt requestTxt) throws
            IOException, MessagingException {
        SDRequest request = signDocumentRequestUseCase.createRequest(acquisition, requestTxt);
        SDRequestTotal requestTotal = SDRequestTotal.builder().data(request).messageId(requestTxt.getMessageId())
                .dateRequest(coreFunctionDate.getDatetime()).build();
        SDResponseTotalWithLog esResponseTotalWithLog = signDocumentRestRepository.getSignature(requestTotal);
        SignDocument signDocument = buildSignature(acquisition, esResponseTotalWithLog.getResponseTotal(),
                requestTxt.getUsrMod());
        save(signDocument);
        signDocument.setInfoReuseCommon(esResponseTotalWithLog.getInfoReuseCommon());
        vinculationUpdateUseCase.markOperation(acquisition.getId(), CODE_SIGNDOCUMENT, Numbers.TWO.getNumber());
        return signDocument;
    }

    public SignDocument buildSignature(Acquisition acquisition, SDResponseTotal responseTotal, String usrMod) {
        SDResponseOk responseOk = responseTotal.getResponseOk();
        if (responseOk == null) {
            signDocumentUseCase.actionsErrors(acquisition, responseTotal.getResponseError());
        } else {
            SDResponseDatum esResponseDatum = responseOk.getData();
            return SignDocument.builder().createdBy(usrMod).createdDate(coreFunctionDate.getDatetime())
                    .acquisition(acquisition).file(esResponseDatum.getFile()).build();
        }
        return null;
    }
}
