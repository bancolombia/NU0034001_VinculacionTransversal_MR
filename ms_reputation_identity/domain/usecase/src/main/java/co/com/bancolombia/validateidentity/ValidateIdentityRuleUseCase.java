package co.com.bancolombia.validateidentity;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.model.validateidentity.ValidateIdentity;
import co.com.bancolombia.model.validateidentity.ValidateIdentitySave;
import co.com.bancolombia.model.validateidentity.ValidateIdentityScore;

import java.util.Map;

public interface ValidateIdentityRuleUseCase {

    ValidateIdentity startValidateIdentityRule(ValidateIdentitySave validate,
                                               ValidateIdentityReply validateReply,
                                               AcquisitionReply acquisitionReply,
                                               ValidateIdentityRulesReply rules);

    ValidateIdentity validateAccumulated(Double accumulated, AcquisitionReply acquisitionReply,
                                         Map<String, Double> configuration,
                                         ValidateIdentityScore validateIdentityScore);

    String convertBooleanString(Boolean value);

}
