package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CUSTOMER_DOCUMENT_PERSISTENCE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOC_SEND_CONTINGENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_COMPLETED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.CODE_ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.DETAIL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_DOC_PUBLICATION_PROCESS;

@RequiredArgsConstructor
public class PersistenceValidateDocUseCaseImpl implements PersistenceValidateDocUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final Exceptions exceptions;
    private final PersistenceValidationsUseCase persistenceValidationsUseCase;

    @Override
    public String validateError(PersistenceDocumentList persistenceDocument, AcquisitionReply acquisition,
                                String typeDocument) {
        String code = persistenceDocument.getErrorCode();
        String description = persistenceDocument.getErrorDescription();
        vinculationUpdateUseCase.markOperation(UUID.fromString(acquisition.getAcquisitionId()),
                Numbers.THREE.getNumber(), CODE_CUSTOMER_DOCUMENT_PERSISTENCE);
        vinculationUpdateUseCase.updateAcquisition(acquisition.getAcquisitionId(), Numbers.SIX.getNumber());
        String complement = CODE_ERROR.concat(code).concat(SPACE).concat(DETAIL).concat(description);
        if (typeDocument != null) {
            exceptions.createException(null, typeDocument, complement, ERROR_CODE_DOC_PUBLICATION_PROCESS);
        }
        return complement;
    }

    @Override
    public void validateDocument(List<PersistenceDocumentList> data, AcquisitionReply acquisition, String codeDocument,
                                 String typeDocument) {
        Optional<PersistenceDocumentList> persistenceDocumentFounded = data.stream().filter(
                persistenceDocument -> persistenceDocument.getSubTypeDocumentary().equals(codeDocument)).findFirst();
        if (persistenceDocumentFounded.isPresent()) {
            if (OK_STATE.equals(persistenceDocumentFounded.get().getStatus())
                    || DOC_SEND_CONTINGENCY.equals(persistenceDocumentFounded.get().getErrorCode())) {
                persistenceValidationsUseCase.markState(persistenceDocumentFounded.get(), acquisition, ST_COMPLETED);
            } else if (ERROR_STATE.equals(persistenceDocumentFounded.get().getStatus())) {
                validateError(persistenceDocumentFounded.get(), acquisition, typeDocument);
            }
        }
    }
}
