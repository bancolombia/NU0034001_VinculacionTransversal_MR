package co.com.bancolombia.uploaddocument;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.model.sqs.SqsMessageParamAllObject;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CIIU;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CIIU_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_RUT_RETRIES_FLAG_EMPTY;

@RequiredArgsConstructor
public class UploadDocumentRutModifyCiiuUseCaseImpl implements UploadDocumentRutModifyCiiuUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final NaturalPersonUseCase naturalPersonUseCase;
    private final UploadDocumentValidateErrors uploadDocumentValidateErrors;

    @Override
    public boolean modifyCiiu(String ciiu, Acquisition acquisition,
            String userTransaction, SqsMessageParamAllObject sqsMessageParamAllObject) {

        String ciiuCatalog = transformKofaxMainActivityField(ciiu);
        if (ciiuCatalog.isEmpty()) {
            uploadDocumentValidateErrors.validateExceptionRutRetries(
                    acquisition, TYPE_RUT, VALIDATE_RUT_RETRIES_FLAG_EMPTY, sqsMessageParamAllObject);
        }

        EmptyReply reply = naturalPersonUseCase.updateCiiu(acquisition.getId(), ciiuCatalog, userTransaction);
        return reply.isValid();
    }

    private String transformKofaxMainActivityField(String ciiu) {
        CatalogReply reply = vinculationUpdateUseCase.findCatalog(CIIU_CODE.concat(ciiu), CIIU);
        return reply.isValid() ? reply.getCode() : EMPTY;
    }
}
