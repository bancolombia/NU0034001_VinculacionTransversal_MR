package co.com.bancolombia.persistencedocument;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
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
public class RetriesPersistenceDocumentUseCaseImpl implements RetriesPersistenceDocumentUseCase {

    private final PersistenceValidationsRetriesUseCase pVRetriesUC;

    @Override
    public void processResponse(AcquisitionReply acquisition, PersistenceDocument persistenceDocumentWithLog) {
        boolean flagCC = persistenceDocumentWithLog.getData().stream().anyMatch(
                perDocument -> DOCUMENTARY_TYPE.equals(perDocument.getTypeDocumentary()) &&
                        CEDULA_SUBTYPE.equals(perDocument.getSubTypeDocumentary()));

        boolean flagRutDoc = persistenceDocumentWithLog.getData().stream().anyMatch(
                perDocument -> DOCUMENTARY_TYPE.equals(perDocument.getTypeDocumentary()) &&
                        RUT_SUBTYPE.equals(perDocument.getSubTypeDocumentary()));

        if (flagCC && flagRutDoc) {
            List<ErrorField> errorFields;
            errorFields = validateCcAndRut(acquisition, persistenceDocumentWithLog.getData());
            if (!errorFields.isEmpty()) {
                HashMap<String, List<ErrorField>> error = new HashMap<>();
                error.put(ERROR_CODE_DOC_PUBLICATION_PROCESS, errorFields);
            }
        } else if (flagCC) {
            pVRetriesUC.validateDocument(persistenceDocumentWithLog.getData(), acquisition, CEDULA_SUBTYPE);
        } else if (flagRutDoc) {
            pVRetriesUC.validateDocument(persistenceDocumentWithLog.getData(), acquisition, RUT_SUBTYPE);
        }
    }

    public List<ErrorField> validateCcAndRut(AcquisitionReply acquisition, List<PersistenceDocumentList> data) {
        List<ErrorField> errorFields = new ArrayList<>();
        Optional<PersistenceDocumentList> persistenceDocumentFoundCed = data.stream()
                .filter(p -> CEDULA_SUBTYPE.equals(p.getSubTypeDocumentary())).findFirst();
        if (persistenceDocumentFoundCed.isPresent()) {
            if (OK_STATE.equals(persistenceDocumentFoundCed.get().getStatus()) ||
                    DOC_SEND_CONTINGENCY.equals(persistenceDocumentFoundCed.get().getErrorCode())) {
                pVRetriesUC.markState(persistenceDocumentFoundCed.get(), acquisition, ST_PENDING);

                Optional<PersistenceDocumentList> persistenceDocumentFoundRUTdoc = data.stream()
                        .filter(p -> RUT_SUBTYPE.equals(p.getSubTypeDocumentary())).findFirst();
                if (persistenceDocumentFoundRUTdoc.isPresent()) {
                    if (OK_STATE.equals(persistenceDocumentFoundRUTdoc.get().getStatus()) ||
                            DOC_SEND_CONTINGENCY.equals(persistenceDocumentFoundRUTdoc.get().getErrorCode())) {
                        pVRetriesUC.markState(persistenceDocumentFoundRUTdoc.get(), acquisition, ST_COMPLETED);
                    } else if (ERROR_STATE.equals(persistenceDocumentFoundRUTdoc.get().getStatus())) {
                        String complement = pVRetriesUC.validateError(persistenceDocumentFoundRUTdoc.get());
                        errorFields.add(ErrorField.builder().name(TYPE_RUT).complement(complement).build());
                    }
                }
            } else if (ERROR_STATE.equals(persistenceDocumentFoundCed.get().getStatus())) {
                String complement = pVRetriesUC.validateError(persistenceDocumentFoundCed.get());
                errorFields.add(ErrorField.builder().name(TYPE_CC).complement(complement).build());
                errorFields.add(ErrorField.builder().name(TYPE_RUT).complement(complement).build());
            }
        }
        return errorFields;
    }
}
