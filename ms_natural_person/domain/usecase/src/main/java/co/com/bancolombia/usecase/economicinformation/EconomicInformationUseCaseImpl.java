package co.com.bancolombia.usecase.economicinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.economicinformation.EconomicInformation;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.commons.ValidateInfoGeneric;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ECONOMIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ST_OPE_COMPLETADO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.FINISH_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MIDDLE_SCREEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MY_APP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NOT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OPER_ECONOMIC_INF;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARAMETER_REQUIRED_RUT_OCCUPATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVICE_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.START_OPERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.YES;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.ERROR_CODE_NON_MODIFIABLE;

public class EconomicInformationUseCaseImpl
        extends ValidateInfoGeneric<EconomicInformation, ExecFieldReply, DependentFieldUseCase>
        implements EconomicInformationUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateCatalogsEconomicUseCase validateCatalogsEconomicUseCase;
    private final MergeUseCase mergeUseCase;
    private final ParametersUseCase parametersUseCase;
    private final EconomicInformationRepository economicInformationRepository;

    private final LoggerAdapter adapter = new LoggerAdapter(MY_APP, SERVICE_INFORMATION, OPER_ECONOMIC_INF);

    public EconomicInformationUseCaseImpl(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsEconomicUseCase validateCatalogsEconomicUseCase,
            MergeUseCase mergeUseCase,
            ParametersUseCase parametersUseCase,
            EconomicInformationRepository economicInformationRepository) {
        super(dependentFieldUseCase);
        this.economicInformationRepository = economicInformationRepository;
        this.validateCatalogsEconomicUseCase = validateCatalogsEconomicUseCase;
        this.vinculationUpdateUseCase = vinculationUpdateUseCase;
        this.mergeUseCase = mergeUseCase;
        this.parametersUseCase = parametersUseCase;
    }

    @Override
    public String economicActivityMark(String economicActivity) {
        if (economicActivity != null) {
            return YES;
        }
        return NOT;
    }

    @Override
    public EconomicInformation startProcessEconomicInformation(EconomicInformation economicInformation) {
        adapter.info(START_OPERATION);
        validateCatalogsEconomicUseCase.validateEconomicInfoCatalogs(economicInformation);
        EconomicInformation info = this.economicInformationRepository
                .findByAcquisition(economicInformation.getAcquisition());
        if (info != null) {
            MergeAttrib mergeAttrib = MergeAttrib.builder().stepCode(CODE_ECONOMIC_INFO).nameList(null)
                    .isRecordUpgradeable(false).build();
            List<ErrorField> upErrFiel = this.mergeUseCase.merge(info, economicInformation, mergeAttrib);
            if (!upErrFiel.isEmpty()) {
                HashMap<String, List<ErrorField>> error = new HashMap<>();
                error.put(ERROR_CODE_NON_MODIFIABLE, upErrFiel);
                adapter.error(ERROR+ERROR_CODE_NON_MODIFIABLE+MIDDLE_SCREEN+upErrFiel);
                throw new ValidationException(error);
            }
            info.setAcquisition(economicInformation.getAcquisition());
            info.setUpdatedBy(economicInformation.getCreatedBy());
            info.setUpdatedDate(economicInformation.getCreatedDate());
        } else {
            info = economicInformation;
        }
        this.validateMandatory(info);
        info.setRequiredRut( this.parametersUseCase.findByCodeAndParent(info.getOccupation(),
                PARAMETER_REQUIRED_RUT_OCCUPATION).isPresent() ? YES : NOT);
        vinculationUpdateUseCase.markOperation(
                info.getAcquisition().getId(), CODE_ECONOMIC_INFO, CODE_ST_OPE_COMPLETADO);
        info.setEconomicActivityMark(this.economicActivityMark(info.getEconomicActivity()));
        adapter.info(FINISH_OPERATION);
        return this.economicInformationRepository.save(info);
    }

    public void validateMandatory(EconomicInformation info) {
        List<ExecFieldReply> mandatoryExecFList = vinculationUpdateUseCase
                .checkListStatus(info.getAcquisition().getId(), CODE_ECONOMIC_INFO).getExecFieldList()
                .stream().filter(ExecFieldReply::isMandatory).collect(Collectors.toList());

        this.validateMandatoryFields(info, mandatoryExecFList, null,
                DependentFieldParamValidator.builder()
                        .acquisition(info.getAcquisition())
                        .operation(OPER_ECONOMIC_INF).build());
    }

    @Override
    public EconomicInformation save(EconomicInformation economicInformation){
        return economicInformationRepository.save(economicInformation);
    }

    @Override
    public Optional<EconomicInformation> findByAcquisition(Acquisition acquisition){
        return Optional.ofNullable(economicInformationRepository.findByAcquisition(acquisition));
    }
}
