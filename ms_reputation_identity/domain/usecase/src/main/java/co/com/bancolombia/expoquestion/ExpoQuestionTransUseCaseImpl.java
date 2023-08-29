package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.expoquestion.AlertIdentification;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;
import co.com.bancolombia.model.expoquestion.ValidateIdentification;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationAlert;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationResponse;
import co.com.bancolombia.model.expoquestion.ValidateIdentificationSave;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExpoQuestionTransUseCaseImpl implements ExpoQuestionTransUseCase {
    private final CoreFunctionDate coreFD;

    @Override
    public ExpoQuestionSave transExpoQuestionSave(AcquisitionReply acquisitionReply, BasicAcquisitionRequest bar,
                                                  ValidateIdentificationResponse identificationResponse) {
        return ExpoQuestionSave.builder().idAcquisition(acquisitionReply.getAcquisitionId())
                .validateIdentification(transVIdentificationSave(identificationResponse))
                .questionnaire(QuestionnaireSave.builder().build()).createdBy(bar.getMessageId()).build();
    }

    @Override
    public List<ValidateIdentificationAlert> transVIdentificationAlerts(List<AlertIdentification> alerts) {
        List<ValidateIdentificationAlert> alertsList = new ArrayList<>();
        if (alerts != null) {
            alerts.forEach(a -> alertsList.add(ValidateIdentificationAlert.builder().alertCode(a.getAlertCode())
                    .customerAlerts(a.getCustomerAlerts()).responseToAlert(a.getResponseToAlert()).build()));
        }
        return alertsList;
    }

    @Override
    public ValidateIdentificationSave transVIdentificationSave(ValidateIdentificationResponse identificationResponse) {
        ValidateIdentification identification = identificationResponse.getData().get(0);
        return ValidateIdentificationSave.builder()
                .validationIdentifier(identification.getValidationIdentifier())
                .identificationNumber(identification.getIdentificationNumber())
                .identificationType(identification.getIdentificationType())
                .dateOfIssue(identification.getDateOfIssue())
                .names(identification.getNames())
                .surnameValidation(identification.getSurnameValidation())
                .secondSurnameValidation(identification.getSecondSurnameValidation())
                .namesValidation(identification.getNamesValidation())
                .dateOfIssueValidation(identification.getDateOfIssueValidation())
                .excludeCustomerParametrized(identification.getExcludeCustomerParametrized())
                .numberParameterizedAttemps(identification.getNumberParameterizedAttemps())
                .processResult(identification.getProcessResult())
                .resultCodeValidation(identification.getResultCodeValidation())
                .causeCodeValidation(identification.getCauseCodeValidation())
                .inquiriesAvailable(identification.getInquiriesAvailable())
                .messageId(identificationResponse.getMeta().getMessageId())
                .requestDate(identificationResponse.getMeta().getRequestDate())
                .createdDate(coreFD.getDatetime())
                .alerts(transVIdentificationAlerts(identification.getAlertsValidateIdentification()))
                .build();
    }
}
