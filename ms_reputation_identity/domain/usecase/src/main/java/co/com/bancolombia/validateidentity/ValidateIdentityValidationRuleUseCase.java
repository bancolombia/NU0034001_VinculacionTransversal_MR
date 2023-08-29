package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRuleReply;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ValidateIdentityValidationRuleUseCase {

    BigDecimal getBigDecimal(Double num);

    ValidateIdentityScore calculateAccumulated(List<ValidateIdentityRuleReply> listValidateIdentityRules,
                                               ValidateIdentitySave validateIdentityAllRegister,
                                               ValidateIdentityReply infoNecesaryValidateRule,
                                               Map<String, Double> configuration);
}
