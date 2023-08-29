package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PROCESS_DOCUMENTS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_STATUS_STEP_DILIGENCIAMIENTO_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DEFAULD_THRESHOLD_PHONETHICS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOUBLE_ZERO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NAME_VAL_IDENTITY_UPD_MANUAL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NUMBER_ONE_HUNDRED;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PARENT_VIGENT_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MIN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_PHONETHICS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDATE_MATCH_EMAIL_CELL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsErrors.VALIDATE_IDENTITY_CODE_ERROR_DOCUMENT_NOT_VIGENT;

@RequiredArgsConstructor
public class ValidateIdentityRuleUtilUseCaseImpl implements ValidateIdentityRuleUtilUseCase {

    private final ParametersRepository parametersRepository;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    @Override
    public void validateVigenceInParameters(AcquisitionReply acquisitionReply, String codeStatus) {
        List<Parameter> parameters = this.parametersRepository.findByParent(PARENT_VIGENT_IDENTITY);
        if (parameters.stream().noneMatch(item -> item.getCode().equals(codeStatus))) {
            refuseAcquisition(acquisitionReply);
            HashMap<String, List<ErrorField>> error = new HashMap<>();
            error.put(VALIDATE_IDENTITY_CODE_ERROR_DOCUMENT_NOT_VIGENT, Collections
                    .singletonList(ErrorField.builder().build()));
            throw new ValidationException(error);
        }
    }

    @Override
    public List<ValidateIdentityRuleReply> findRulesActive(List<ValidateIdentityRuleReply> list) {
        return list.stream().filter(data -> "true".equals(data.getActive())).collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> findConfiguration(String typeAcquisition) {
        Map<String, Double> rules = new HashMap<>();
        List<Parameter> listParameterValidateIdentity = parametersRepository
                .findByParentTypeAcquisition(PARENT_VALIDATE_IDENTITY,typeAcquisition);
        listParameterValidateIdentity.forEach(a -> {
            if (a.getName().equals(THRESHOLD_MIN)) {
                rules.put(THRESHOLD_MIN, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(THRESHOLD_MAX)) {
                rules.put(THRESHOLD_MAX, Double.valueOf(a.getCode()));
            }
            if (a.getName().equals(THRESHOLD_PHONETHICS)) {
                rules.put(THRESHOLD_PHONETHICS, Double.valueOf(a.getCode()));
            }
        });
        return rules;
    }

    @Override
    public void refuseAcquisition(AcquisitionReply acquisitionReply) {
        vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(), CODE_VALIDATE_IDENTITY,
                Numbers.THREE.getNumber());
        vinculationUpdateUseCase.updateAcquisition(acquisitionReply.getAcquisitionId(), Numbers.TWO.getNumber());
    }

    @Override
    public ValidateIdentityRuleReply validateIfApplyRule(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                                         String nameRule) {
        return listValidateIdentityRules.stream().filter(p -> p.getName().equals(nameRule))
                .findFirst().orElse(null);
    }

    @Override
    public boolean isInRange(String ageMin, String ageMax, Integer age) {
        IntStream intStream = IntStream.rangeClosed(Integer.parseInt(ageMin), Integer.parseInt(ageMax));
        return intStream.anyMatch(p -> p == age);
    }

    @Override
    public Double getConfThresholdPhonethics(Map<String, Double> configuration) {
        return configuration.get(THRESHOLD_PHONETHICS) != null ?
                configuration.get(THRESHOLD_PHONETHICS) :
                DEFAULD_THRESHOLD_PHONETHICS;
    }

    @Override
    public String getFullNameInverse(ValidateIdentityReply infoNecesaryValidateRule) {
        return Stream.of(infoNecesaryValidateRule.getFirstSurname(), infoNecesaryValidateRule.getSecondSurname(),
                        infoNecesaryValidateRule.getFirstName(), infoNecesaryValidateRule.getSecondName())
                .filter(string -> string != null && !string.isEmpty()).collect(Collectors.joining(" "));
    }

    @Override
    public String getNames(ValidateIdentityReply infoNecesaryValidateRule) {
        return Stream.of(infoNecesaryValidateRule.getFirstName(), infoNecesaryValidateRule.getSecondName())
                .filter(cadena -> cadena != null && !cadena.isEmpty()).collect(Collectors.joining(" "));
    }

    @Override
    public boolean compareString(String strOne, String strTwo, Double thresholdPhonethics) {

        if (strOne.equals(strTwo)) {
            return true;
        } else {
            return this.jaroWinklerDistance(strOne, strTwo, thresholdPhonethics);
        }
    }

    @Override
    public boolean statusStepUploadDocumentIsManual(AcquisitionReply acquisition) {
        ChecklistReply checklistReply = vinculationUpdateUseCase.checkListStatus(acquisition.getAcquisitionId(),
                CODE_PROCESS_DOCUMENTS);
        return CODE_STATUS_STEP_DILIGENCIAMIENTO_MANUAL.equals(checklistReply.getStateOperation());
    }

    @Override
    public boolean validateParameterStepUploadDocumentIsManual() {
        Optional<Parameter> parameterActiveUplManual = parametersRepository
                .findByNameParent(NAME_VAL_IDENTITY_UPD_MANUAL, PARENT_VALIDATE_IDENTITY);
        return parameterActiveUplManual.filter(parameter -> Numbers.ONE.getNumber().equals(parameter.getCode()))
                .isPresent();
    }

    @Override
    public boolean validateParameterValidateEmailAndCell() {
        Optional<Parameter> parameterActiveUplManual = parametersRepository
                .findByNameParent(VALIDATE_MATCH_EMAIL_CELL, PARENT_VALIDATE_IDENTITY);
        return parameterActiveUplManual.filter(parameter -> Numbers.ONE.getNumber().equals(parameter.getCode()))
                .isPresent();
    }

    public boolean jaroWinklerDistance(String strOne, String strTwo, Double thresholdPhonethics) {
        JaroWinklerDistance algorithm = new JaroWinklerDistance();
        Double result;
        String[] partsStrOne = strOne.split(" ");
        String[] partsStrTwo = strTwo.split(" ");
        Double acuumutale = DOUBLE_ZERO;
        if (partsStrOne.length == partsStrTwo.length && partsStrOne.length > 1) {
            for (int i = 0; i < partsStrOne.length; i++) {
                Double m = algorithm.apply(partsStrOne[i], partsStrTwo[i]);
                acuumutale += m;
            }
            result = acuumutale / partsStrOne.length;
        } else {
            result = algorithm.apply(strOne, strTwo);
        }
        return (result * NUMBER_ONE_HUNDRED) >= thresholdPhonethics;
    }
}
