package co.com.bancolombia.acquisition;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCase;
import co.com.bancolombia.model.acquisition.AcquisitionStartObjectModel;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_START_ACQUISITION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_MANAGEMENT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SYSTEM_VTN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_WITHOUT_LINK;

@RequiredArgsConstructor
public class AcquisitionUseCaseImpl implements AcquisitionUseCase {
    private final AcquisitionRepository acquisitionRepository;
    private final MatrixAcquisitionUseCase matrixAcquisitionUseCase;
    private final DocumentTypeUseCase documentTypeUseCase;

    LoggerAdapter adapter = new LoggerAdapter(SYSTEM_VTN, SERVICE_MANAGEMENT, OPER_START_ACQUISITION);

    @Override
    public void setAcquisitionMatrices(Acquisition acquisition) {
        if (acquisition.getTypeAcquisition() != null) {
            List<MatrixAcquisition> acquisitionMatrices = this.matrixAcquisitionUseCase
                    .findByTypeAcquisition(acquisition.getTypeAcquisition());

            acquisition.getTypeAcquisition().setAcquisitionMatrices(acquisitionMatrices);
        }
    }

    @Override
    public Acquisition findById(UUID idAcquisition) {
        Acquisition ret = acquisitionRepository.findById(idAcquisition);
        if (ret == null || (!ret.getStateAcquisition().getCode().equals(Numbers.ONE.getNumber()) &&
                !ret.getStateAcquisition().getCode().equals(Numbers.FIVE.getNumber()) &&
                !ret.getStateAcquisition().getCode().equals(Numbers.SIX.getNumber()) &&
                !ret.getStateAcquisition().getCode().equals(Numbers.SEVEN.getNumber())
        )) {
            return null;
        }
        setAcquisitionMatrices(ret);
        return ret;
    }

    /**
     * Returns an Acquisition if exists, otherwise throws an
     * AcquisitionNotFoundException
     */
    @Override
    public Acquisition findAndValidateById(UUID idAcquisition) {
        Acquisition ret = findById(idAcquisition);
        if (ret == null) {
            adapter.error(ERROR + ERROR_CODE_WITHOUT_LINK + ErrorField.builder().build());
            throw new ValidationException(
                    new MapErrorAcquisition(ERROR_CODE_WITHOUT_LINK, ErrorField.builder().build()).map);
        }
        return ret;
    }

    @Override
    public Acquisition findAcquisition(AcquisitionStartObjectModel acquisitionStartObjectModel) {
        Optional<Acquisition> acquisitionOp = acquisitionRepository.findAcquisition(acquisitionStartObjectModel);
        return acquisitionOp.orElse(null);
    }

    @Override
    public Acquisition findByIdAndDocumentTypeAndDocumentNumber(UUID id, String documentType, String documentNumber) {
        Optional<DocumentType> dcType = documentTypeUseCase.findByCode(documentType);
        List<Acquisition> ret = new ArrayList<>();
        if (dcType.isPresent()) {
            ret = acquisitionRepository.findByIdAndDocumentTypeAndDocumentNumber(id, dcType.get(), documentNumber);
        }

        return ret.isEmpty() ? null : ret.get(0);
    }

    @Override
    public Acquisition findByDocumentTypeAndDocumentNumber(String documentType, String documentNumber) {
        Optional<DocumentType> dcType = documentTypeUseCase.findByCode(documentType);
        List<Acquisition> ret = new ArrayList<>();
        if (dcType.isPresent()) {
            ret = acquisitionRepository.findByDocumentTypeAndDocumentNumber(dcType.get(), documentNumber);
        }

        return ret.isEmpty() ? null : ret.get(0);
    }

    @Override
    public Acquisition findByIdWitOutState(UUID idAcquisition) {
        return acquisitionRepository.findById(idAcquisition);
    }
}
