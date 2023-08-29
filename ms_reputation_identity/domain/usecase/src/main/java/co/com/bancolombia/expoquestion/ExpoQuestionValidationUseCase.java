package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;

public interface ExpoQuestionValidationUseCase {

    void validationStageOne(AcquisitionReply acquisitionReply);

    ValidateIdentityReply validateMissingData(AcquisitionReply acquisitionReply);

    void validateException(String exception, String nameValue, String code, String detail);

    String blockCustomer(AcquisitionReply acquisitionReply, String code);
}
