package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DOUBLE_CERO_INIT;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA01;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA02;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA03;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA04;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA05;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA06;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA07;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA08;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA09;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.REGLA10;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZERO;

@RequiredArgsConstructor
public class ValidateIdentityValidationRuleUseCaseImpl implements ValidateIdentityValidationRuleUseCase {

    private final CoreFunctionDate coreFunctionDate;
    private final ValidateIdentityRuleUtilUseCase validateIUtil;

    @Override
    public BigDecimal getBigDecimal(Double num) {
        return BigDecimal.valueOf(num);
    }

    @Override
    public ValidateIdentityScore calculateAccumulated(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                                      ValidateIdentitySave validateIdentityAllRegister,
                                                      ValidateIdentityReply infoNecesaryValidateRule,
                                                      Map<String, Double> configuration) {
        return ValidateIdentityScore.builder()
                .ruleOneCellphone(this.getBigDecimal(this.validateRuleOneCellphone(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleTwoEmail(this.getBigDecimal(this.validateRuleTwoEmail(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleThreeAge(this.getBigDecimal(this.validateRuleThreeAge(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleFourDateExpedition(this.getBigDecimal(this.validateRuleFourDateExpedition(
                        listValidateIdentityRules, validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleFiveFullName(this.getBigDecimal(this.validateRuleFiveFullName(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule, configuration)))
                .ruleSixSecondSurname(this.getBigDecimal(this.validateRuleSixSecondSurname(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule, configuration)))
                .ruleSevenWorkPhone(this.getBigDecimal(this.validateRuleSevenWorkPhone(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleEightWorkCellphone(this.getBigDecimal(this.validateRuleEightWorkCellphone(
                        listValidateIdentityRules, validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleNineWorkEmail(this.getBigDecimal(this.validateRuleNineWorkEmail(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule)))
                .ruleTenNames(this.getBigDecimal(this.validateRuleTenNames(listValidateIdentityRules,
                        validateIdentityAllRegister, infoNecesaryValidateRule, configuration)))
                .accumulated(this.getBigDecimal(0.0)).build();
    }

    public Double validateRuleOneCellphone(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                           ValidateIdentitySave validateIdentityAllRegister,
                                           ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA01);
        if (rulesValidation != null && infoNecesaryValidateRule.getCellphonePersonal() != null) {
            String cellphoneAcquisition = infoNecesaryValidateRule.getCellphonePersonal();
            if (validateIdentityAllRegister.getMobiles().stream()
                    .anyMatch(p -> p.getMobile().equals(cellphoneAcquisition))) {
                result = Double.parseDouble(rulesValidation.getScore());
            }
        }
        return result;
    }

    public Double validateRuleTwoEmail(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                       ValidateIdentitySave validateIdentityAllRegister,
                                       ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA02);
        if (rulesValidation != null && infoNecesaryValidateRule.getEmailPersonal() != null) {
            String emailAcquisition = infoNecesaryValidateRule.getEmailPersonal();
            if (validateIdentityAllRegister.getEmails().stream()
                    .anyMatch(p -> p.getEmail().equals(emailAcquisition))) {
                result = Double.parseDouble(rulesValidation.getScore());
            }
        }
        return result;
    }

    public Double validateRuleThreeAge(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                       ValidateIdentitySave validateIdentityAllRegister,
                                       ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA03);
        if (rulesValidation != null && infoNecesaryValidateRule.getBirthDate() != null) {
            int age = coreFunctionDate.returnAge(infoNecesaryValidateRule.getBirthDate());
            if (validateIUtil.isInRange(validateIdentityAllRegister.getAgeMin(),
                    validateIdentityAllRegister.getAgeMax(), age)) {
                result = Double.parseDouble(rulesValidation.getScore());
            }
        }
        return result;
    }

    public Double validateRuleFourDateExpedition(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                                 ValidateIdentitySave validateIdentityAllRegister,
                                                 ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA04);
        if (rulesValidation != null && infoNecesaryValidateRule.getExpeditionDate() != null) {
            Date dateExpeditionAcquisition = infoNecesaryValidateRule.getExpeditionDate();
            if (validateIdentityAllRegister.getIssueDate()
                    .compareTo(dateExpeditionAcquisition) == ZERO) {
                result = Double.parseDouble(rulesValidation.getScore());
            }
        }
        return result;
    }

    public Double validateRuleFiveFullName(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                           ValidateIdentitySave validateIdentityAllRegister,
                                           ValidateIdentityReply infoNecesaryValidateRule,
                                           Map<String, Double> configuration) {
        Double result = DOUBLE_CERO_INIT;
        Double thresholdPhonethics = validateIUtil.getConfThresholdPhonethics(configuration);
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA05);
        if (rulesValidation != null && validateIUtil.compareString(validateIdentityAllRegister.getFullName(),
                validateIUtil.getFullNameInverse(infoNecesaryValidateRule), thresholdPhonethics)) {
            result = Double.parseDouble(rulesValidation.getScore());
        }
        return result;
    }

    public Double validateRuleSixSecondSurname(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                               ValidateIdentitySave validateIdentityAllRegister,
                                               ValidateIdentityReply infoNecesaryValidateRule,
                                               Map<String, Double> configuration) {
        Double result = DOUBLE_CERO_INIT;
        Double thresholdPhonethics = validateIUtil.getConfThresholdPhonethics(configuration);
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA06);
        if (rulesValidation != null && infoNecesaryValidateRule.getSecondSurname() != null
                && !validateIdentityAllRegister.getSecondSurname().isEmpty()
                && validateIUtil.compareString(validateIdentityAllRegister.getSecondSurname(),
                infoNecesaryValidateRule.getSecondSurname(), thresholdPhonethics)) {
            result = Double.parseDouble(rulesValidation.getScore());

        }
        return result;
    }

    public Double validateRuleSevenWorkPhone(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                             ValidateIdentitySave validateIdentityAllRegister,
                                             ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA07);
        if (rulesValidation != null && infoNecesaryValidateRule.getTelephoneNumber() != null) {
            if (validateIdentityAllRegister.getPhones().stream()
                    .anyMatch(p -> p.getPhoneNumber().equals(infoNecesaryValidateRule.getTelephoneNumber()))) {
                result = Double.parseDouble(rulesValidation.getScore());
            }
        }
        return result;
    }

    public Double validateRuleEightWorkCellphone(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                                 ValidateIdentitySave validateIdentityAllRegister,
                                                 ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA08);
        if (rulesValidation != null && infoNecesaryValidateRule.getCellphoneContact() != null) {
            if (validateIdentityAllRegister.getMobiles().stream()
                    .anyMatch(p -> p.getMobile().equals(infoNecesaryValidateRule.getCellphoneContact()))) {
                result = Double.parseDouble(rulesValidation.getScore());

            }
        }
        return result;
    }

    public Double validateRuleNineWorkEmail(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                            ValidateIdentitySave validateIdentityAllRegister,
                                            ValidateIdentityReply infoNecesaryValidateRule) {
        Double result = DOUBLE_CERO_INIT;
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA09);
        if (rulesValidation != null && infoNecesaryValidateRule.getEmailContact() != null) {
            if (validateIdentityAllRegister.getEmails().stream()
                    .anyMatch(p -> p.getEmail().equals(infoNecesaryValidateRule.getEmailContact()))) {
                result = Double.parseDouble(rulesValidation.getScore());

            }
        }
        return result;
    }

    public Double validateRuleTenNames(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                       ValidateIdentitySave validateIdentityAllRegister,
                                       ValidateIdentityReply infoNecesaryValidateRule,
                                       Map<String, Double> configuration) {
        Double result = DOUBLE_CERO_INIT;
        Double thresholdPhonethics = validateIUtil.getConfThresholdPhonethics(configuration);
        ValidateIdentityRuleReply rulesValidation = validateIUtil.validateIfApplyRule(listValidateIdentityRules,
                REGLA10);
        if (rulesValidation != null && validateIUtil.compareString(validateIdentityAllRegister.getName(),
                validateIUtil.getNames(infoNecesaryValidateRule), thresholdPhonethics)) {
            result = Double.parseDouble(rulesValidation.getScore());
        }
        return result;
    }
}
