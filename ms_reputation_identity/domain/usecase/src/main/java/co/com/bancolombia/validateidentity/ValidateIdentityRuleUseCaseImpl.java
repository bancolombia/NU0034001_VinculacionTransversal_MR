package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityScoreRepository;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_EXPQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_IDENTITY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_VALIDATE_VALIDATEQUESTIONS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOUBLE_CERO_INIT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NUM_DOS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_THREE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_TWO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_NAME_ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_NAME_THREE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_NAME_TWO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MAX;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.THRESHOLD_MIN;

@RequiredArgsConstructor
public class ValidateIdentityRuleUseCaseImpl implements ValidateIdentityRuleUseCase {

    private final ValidateIdentityRuleUtilUseCase validateIRUtilUC;
    private final VinculationUpdateUseCase vinculationUpdateUseCase;
    private final ValidateIdentityValidationRuleUseCase vIValidationRuleUC;
    private final ValidateIdentityScoreRepository scoreRepository;

    @Override
    public ValidateIdentity startValidateIdentityRule(ValidateIdentitySave validate,
                                                      ValidateIdentityReply validateReply,
                                                      AcquisitionReply acquisitionReply,
                                                      ValidateIdentityRulesReply rules) {

        validateIRUtilUC.validateVigenceInParameters(acquisitionReply, validate.getStatus());
        Map<String, Double> configuration = validateIRUtilUC.findConfiguration(rules.getTypeAcquisition());
        ValidateIdentityScore validateIdentityScore = vIValidationRuleUC.calculateAccumulated(
                validateIRUtilUC.findRulesActive(rules.getValidateIdentityRulesList()), validate, validateReply,
                configuration);
        Double accumulated = sumAccumulated(validateIdentityScore);
        validateIdentityScore.setIdAcquisition(acquisitionReply.getAcquisitionId());
        validateIdentityScore.setAccumulated(vIValidationRuleUC.getBigDecimal(getPrecision(accumulated)));
        scoreRepository.save(validateIdentityScore);
        return validateAccumulated(getPrecision(accumulated), acquisitionReply, configuration, validateIdentityScore);
    }

    @Override
    public ValidateIdentity validateAccumulated(Double accumulated, AcquisitionReply acquisitionReply,
                                                Map<String, Double> configuration,
                                                ValidateIdentityScore validateIdentityScore) {
        ValidateIdentity validateIdentity = null;
        if (configuration != null) {
            validateIdentity = this.validateThresholdMin(accumulated, configuration.get(THRESHOLD_MIN),
                    acquisitionReply);
            if (validateIdentity == null) {
                validateIdentity = validateThresholdBetweenMinAndMax(accumulated, configuration.get(THRESHOLD_MIN),
                        configuration.get(THRESHOLD_MAX), acquisitionReply);
            }
            if (validateIdentity == null) {
                validateIdentity = validateThresholdMax(accumulated, configuration.get(THRESHOLD_MAX),
                        acquisitionReply, validateIdentityScore);
            }
        }
        return validateIdentity;
    }

    @Override
    public String convertBooleanString(Boolean value) {
        if (value == null) {
            return "false";
        } else {
            return String.valueOf(value);
        }
    }

    public Double sumAccumulated(ValidateIdentityScore validateIdentityScore) {
        double accumulated = DOUBLE_CERO_INIT;
        accumulated += validateIdentityScore.getRuleOneCellphone().doubleValue();
        accumulated += validateIdentityScore.getRuleTwoEmail().doubleValue();
        accumulated += validateIdentityScore.getRuleThreeAge().doubleValue();
        accumulated += validateIdentityScore.getRuleFourDateExpedition().doubleValue();
        accumulated += validateIdentityScore.getRuleFiveFullName().doubleValue();
        accumulated += validateIdentityScore.getRuleSixSecondSurname().doubleValue();
        accumulated += validateIdentityScore.getRuleSevenWorkPhone().doubleValue();
        accumulated += validateIdentityScore.getRuleEightWorkCellphone().doubleValue();
        accumulated += validateIdentityScore.getRuleNineWorkEmail().doubleValue();
        accumulated += validateIdentityScore.getRuleTenNames().doubleValue();
        return accumulated;
    }

    public ValidateIdentity validateThresholdMin(Double accumulated, Double thresholdMin,
                                                 AcquisitionReply acquisitionReply) {
        ValidateIdentity infoReturnValidateIdentity = null;
        if (accumulated < thresholdMin) {
            validateIRUtilUC.refuseAcquisition(acquisitionReply);
            infoReturnValidateIdentity = ValidateIdentity.builder().outComeCode(OUT_COME_CODE_THREE)
                    .outComeName(OUT_COME_NAME_THREE).matchPercentaje(accumulated).build();
        }
        return infoReturnValidateIdentity;
    }

    public ValidateIdentity validateThresholdBetweenMinAndMax(Double accumulated, Double thresholdMin,
                                                              Double thresholdMax, AcquisitionReply acquisitionReply) {
        ValidateIdentity infoReturnValidateIdentity = null;
        if (accumulated >= thresholdMin && accumulated <= thresholdMax) {
            vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(), CODE_VALIDATE_IDENTITY,
                    Numbers.FOUR.getNumber());
            infoReturnValidateIdentity = ValidateIdentity.builder().outComeCode(OUT_COME_CODE_TWO)
                    .outComeName(OUT_COME_NAME_TWO).matchPercentaje(accumulated).build();
        }
        return infoReturnValidateIdentity;
    }

    public ValidateIdentity scaleOperation(String idAcquisition, Double accumulated) {
        vinculationUpdateUseCase.markOperation(idAcquisition, CODE_VALIDATE_IDENTITY,
                Numbers.FOUR.getNumber());
        return ValidateIdentity.builder().outComeCode(OUT_COME_CODE_TWO)
                .outComeName(OUT_COME_NAME_TWO).matchPercentaje(accumulated).build();
    }

    public ValidateIdentity validateThresholdMax(Double accumulated, Double thresholdMax,
                                                 AcquisitionReply acquisitionReply,
                                                 ValidateIdentityScore validateIdentityScore) {
        ValidateIdentity infoReturnValidateIdentity = null;
        if (accumulated > thresholdMax) {
            if (validateIRUtilUC.statusStepUploadDocumentIsManual(acquisitionReply) && validateIRUtilUC
                    .validateParameterStepUploadDocumentIsManual()) {
                infoReturnValidateIdentity = scaleOperation(acquisitionReply.getAcquisitionId(), accumulated);
                infoReturnValidateIdentity.setDocumentManual(true);
                return infoReturnValidateIdentity;
            } else {
                if (validateIRUtilUC.validateParameterValidateEmailAndCell() &&
                        validateIdentityScore.getRuleOneCellphone().compareTo(new BigDecimal(0)) == 0 &&
                        validateIdentityScore.getRuleTwoEmail().compareTo(new BigDecimal(0)) == 0) {
                    infoReturnValidateIdentity = scaleOperation(acquisitionReply.getAcquisitionId(), accumulated);
                    infoReturnValidateIdentity.setEmailAndCellError(true);
                    return infoReturnValidateIdentity;
                }
            }
            vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(), CODE_VALIDATE_IDENTITY,
                    Numbers.TWO.getNumber());
            vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(), CODE_VALIDATE_EXPQUESTIONS,
                    Numbers.SIX.getNumber());
            vinculationUpdateUseCase.markOperation(acquisitionReply.getAcquisitionId(), CODE_VALIDATE_VALIDATEQUESTIONS,
                    Numbers.SIX.getNumber());
            infoReturnValidateIdentity = ValidateIdentity.builder().outComeCode(OUT_COME_CODE_ONE)
                    .outComeName(OUT_COME_NAME_ONE).matchPercentaje(accumulated).build();
        }
        return infoReturnValidateIdentity;
    }

    public Double getPrecision(Double accumulated) {
        return Precision.round(accumulated, NUM_DOS);
    }
}
