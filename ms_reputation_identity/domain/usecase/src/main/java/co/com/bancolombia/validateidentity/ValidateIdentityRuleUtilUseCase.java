package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;

import java.util.List;
import java.util.Map;

public interface ValidateIdentityRuleUtilUseCase {

    void validateVigenceInParameters(AcquisitionReply acquisitionReply, String codeStatus);

    List<ValidateIdentityRuleReply> findRulesActive(List<ValidateIdentityRuleReply> list);

    Map<String, Double> findConfiguration(String typeAcquisition);

    ValidateIdentityRuleReply validateIfApplyRule(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                                  String nameRule);

    void refuseAcquisition(AcquisitionReply acquisitionReply);

    boolean isInRange(String ageMin, String ageMax, Integer age);

    Double getConfThresholdPhonethics(Map<String, Double> configuration);

    String getFullNameInverse(ValidateIdentityReply infoNecesaryValidateRule);

    String getNames(ValidateIdentityReply infoNecesaryValidateRule);

    boolean compareString(String strOne, String strTwo, Double thresholdPhonethics);

    boolean statusStepUploadDocumentIsManual(AcquisitionReply acquisition);

    boolean validateParameterStepUploadDocumentIsManual();

    boolean validateParameterValidateEmailAndCell();
}
