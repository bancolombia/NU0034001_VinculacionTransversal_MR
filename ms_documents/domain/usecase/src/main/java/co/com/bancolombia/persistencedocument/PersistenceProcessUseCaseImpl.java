package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.persistencedocument.PersistenceDocument;
import co.com.bancolombia.model.persistencedocument.PersistenceDocumentList;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CEDULA_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOCUMENTARY_TYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOC_SEND_CONTINGENCY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ERROR_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OK_STATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.RUT_SUBTYPE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_COMPLETED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ST_PENDING;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TYPE_RUT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_DOC_PUBLICATION_PROCESS;
import static co.com.bancolombia.util.constants.Constants.TYPE_CC;

@RequiredArgsConstructor
public class PersistenceProcessUseCaseImpl implements PersistenceProcessUseCase {

    private final PersistenceValidationsUseCase pValidationsUC;
    private final PersistenceValidateDocUseCase pValDocUC;

    @Override
    public void processResponse(AcquisitionReply acquisition, PersistenceDocument persistenceDocumentWithLog) {
        boolean flagCC = persistenceDocumentWithLog.getData().stream().anyMatch(
                persistenceDocument -> DOCUMENTARY_TYPE.equals(persistenceDocument.getTypeDocumentary()) &&
                        CEDULA_SUBTYPE.equals(persistenceDocument.getSubTypeDocumentary()));
        boolean flagRut = persistenceDocumentWithLog.getData().stream().anyMatch(
                persistenceDocument -> DOCUMENTARY_TYPE.equals(persistenceDocument.getTypeDocumentary()) &&
                        RUT_SUBTYPE.equals(persistenceDocument.getSubTypeDocumentary()));
        if (flagCC && flagRut) {
            List<ErrorField> errorFields;
            errorFields = validateCcAndRut(acquisition, persistenceDocumentWithLog.getData());
            if (!errorFields.isEmpty()) {
                HashMap<String, List<ErrorField>> error = new HashMap<>();
                error.put(ERROR_CODE_DOC_PUBLICATION_PROCESS, errorFields);
                throw new ValidationException(error);
            }
        } else if (flagCC) {
            pValDocUC.validateDocument(persistenceDocumentWithLog.getData(), acquisition, CEDULA_SUBTYPE, TYPE_CC);
        } else if (flagRut) {
            pValDocUC.validateDocument(persistenceDocumentWithLog.getData(), acquisition, RUT_SUBTYPE, TYPE_RUT);
        }
    }

    public List<ErrorField> validateCcAndRut(AcquisitionReply acquisition, List<PersistenceDocumentList> data) {
        List<ErrorField> errorFields = new ArrayList<>();
        Optional<PersistenceDocumentList> persistenceDocumentFoundCC = data.stream()
                .filter(p -> CEDULA_SUBTYPE.equals(p.getSubTypeDocumentary())).findFirst();

        if (persistenceDocumentFoundCC.isPresent()) {
            if (OK_STATE.equals(persistenceDocumentFoundCC.get().getStatus()) ||
                    DOC_SEND_CONTINGENCY.equals(persistenceDocumentFoundCC.get().getErrorCode())) {
                pValidationsUC.markState(persistenceDocumentFoundCC.get(), acquisition, ST_PENDING);

                Optional<PersistenceDocumentList> pDFoundRUT = data.stream()
                        .filter(p -> RUT_SUBTYPE.equals(p.getSubTypeDocumentary())).findFirst();
                if (pDFoundRUT.isPresent()) {
                    if (OK_STATE.equals(pDFoundRUT.get().getStatus()) ||
                            DOC_SEND_CONTINGENCY.equals(pDFoundRUT.get().getErrorCode())) {
                        pValidationsUC.markState(pDFoundRUT.get(), acquisition, ST_COMPLETED);
                    } else if (ERROR_STATE.equals(pDFoundRUT.get().getStatus())) {
                        String complement = pValDocUC.validateError(pDFoundRUT.get(), acquisition, null);
                        errorFields.add(ErrorField.builder().name(TYPE_RUT).complement(complement).build());
                    }
                }
            } else if (ERROR_STATE.equals(persistenceDocumentFoundCC.get().getStatus())) {
                String complement = pValDocUC.validateError(persistenceDocumentFoundCC.get(), acquisition, null);
                errorFields.add(ErrorField.builder().name(TYPE_CC).complement(complement).build());
                errorFields.add(ErrorField.builder().name(TYPE_RUT).complement(complement).build());
            }
        }
        return errorFields;
    }
}
