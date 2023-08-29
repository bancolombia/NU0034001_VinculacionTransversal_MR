package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionStateCountQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.query.AcquisitionUpdateQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionStateCountReply;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRabbitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOES_NOT_EXIST;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.util.constants.Constants.ACQUISITION;

@Component
@AllArgsConstructor
public class AcquisitionRabbit extends ErrorHandleRabbit implements AcquisitionRabbitRepository {

    private final AcquisitionValidateUseCase acquisitionValidateUseCase;
    private final AcquisitionOperationUseCase acquisitionOperationUseCase;
    private final AcquisitionUseCase acquisitionUseCase;

    @Override
    public AcquisitionReply getAndValidateAcquisition(AcquisitionQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Optional<Acquisition> acquisition = acquisitionValidateUseCase.validateInfoSearchAndGet(
                    query.getAcquisitionId(), query.getDocumentType(), query.getDocumentNumber(), query.getOperation());

            if (!acquisition.isPresent()) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }

            Acquisition acq = acquisition.get();
            AcquisitionReply reply = AcquisitionReply.builder()
                    .valid(true)
                    .acquisitionId(acq.getId().toString())
                    .documentType(acq.getDocumentType().getCode())
                    .documentNumber(acq.getDocumentNumber())
                    .documentTypeHomologous(acq.getDocumentType().getCodeHomologation())
                    .documentTypeOrderExperian(acq.getDocumentType().getCodeOrderExperian())
                    .documentTypeCodeGenericType(acq.getDocumentType().getCodeGenericType())
                    .documentTypeOrderControlList(acq.getDocumentType().getCodeOrderControlList())
                    .stateCode(acq.getStateAcquisition().getCode())
                    .stateName(acq.getStateAcquisition().getName())
                    .codeTypeAcquisition(acq.getTypeAcquisition().getCode())
                    .typePerson(acq.getTypePerson())
                    .createdDate(acq.getCreatedDate())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            AcquisitionReply reply = AcquisitionReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .documentType(query == null ? "" : query.getDocumentType())
                    .documentNumber(query == null ? "" : query.getDocumentNumber())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    @Override
    public EmptyReply updateStateAcquisition(AcquisitionUpdateQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acq = acquisitionUseCase.findById(UUID.fromString(query.getAcquisitionId()));

            if (acq == null) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }

            acquisitionOperationUseCase.updateAcquisition(acq, query.getStateCode());
            EmptyReply reply = EmptyReply.builder()
                    .valid(true)
                    .acquisitionId(query.getAcquisitionId())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            EmptyReply reply = EmptyReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    @Override
    public EmptyReply updateAcquisition(AcquisitionIdQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());
            Acquisition acq = acquisitionUseCase.findById(UUID.fromString(query.getAcquisitionId()));
            if (acq == null) {
                adapter.error(ERROR + SPACE + ACQUISITION + SPACE + query.getAcquisitionId() + SPACE + DOES_NOT_EXIST);
                throwExceptionCodeWithoutLink();
            }
            acquisitionOperationUseCase.save(acq);
            return EmptyReply.builder().valid(true).acquisitionId(query.getAcquisitionId()).build();
        } catch (ValidationException | CustomException ex) {
            return EmptyReply.builder().valid(false).acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex)).build();
        }
    }

    @Override
    public AcquisitionStateCountReply countAcquisitionByState(AcquisitionStateCountQuery query) {
        try {
            validateMandatory(query);
            long result = acquisitionOperationUseCase.countAcquisitionByState(query.getState(),
                    query.getAcquisitionId());
            return AcquisitionStateCountReply.builder()
                    .valid(true)
                    .acquisitionNumber(result)
                    .build();
        } catch (ValidationException | CustomException ex) {
            return AcquisitionStateCountReply.builder()
                    .valid(false)
                    .errorList(getErrorFromException(ex))
                    .build();
        }
    }

    private void validateMandatory(AcquisitionUpdateQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getStateCode() == null || query.getStateCode().isEmpty()) {
                fieldList.add("stateCode");
            }
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
        }
        errorMandatory(fieldList);
    }

    private void validateMandatory(AcquisitionQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
            if (query.getDocumentType() == null || query.getDocumentType().isEmpty()) {
                fieldList.add("documentType");
            }
            if (query.getDocumentNumber() == null || query.getDocumentNumber().isEmpty()) {
                fieldList.add("documentNumber");
            }
            if (query.getOperation() == null || query.getOperation().isEmpty()) {
                fieldList.add("operation");
            }
        }
        errorMandatory(fieldList);
    }

    private void validateMandatory(AcquisitionStateCountQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getState() == null || query.getState().isEmpty()) {
                fieldList.add("state");
            }
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
        }
        errorMandatory(fieldList);
    }

    private void validateMandatory(AcquisitionIdQuery query) {
        List<String> fieldList = new ArrayList<>();
        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
        }
        errorMandatory(fieldList);
    }
}
