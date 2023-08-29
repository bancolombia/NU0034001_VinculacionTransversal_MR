package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.AlertIdentification;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationAlert;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationSave;

import java.util.List;

public interface ExpoQuestionTransUseCase {

    ExpoQuestionSave transExpoQuestionSave(AcquisitionReply acquisitionReply, BasicAcquisitionRequest basicAcqRequest,
                                           ValidateIdentificationResponse identificationResponse);

    List<ValidateIdentificationAlert> transVIdentificationAlerts(List<AlertIdentification> alerts);

    ValidateIdentificationSave transVIdentificationSave(ValidateIdentificationResponse identificationResponse);
}
