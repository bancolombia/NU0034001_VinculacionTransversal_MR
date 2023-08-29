package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.MarkRevokeReply;
import co.com.bancolombia.model.markrevoke.MarkRevoke;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRabbitRepository;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MarkRevokeRabbit extends ErrorHandleRabbit implements MarkRevokeRabbitRepository {

    private final MarkRevokeRepository markRevokeRepository;

    @Override
    public MarkRevokeReply getMarkRevoke(AcquisitionIdQuery query) {
        try {
            validateMandatory(query);
            validateUUID(query.getAcquisitionId());

            Acquisition acquisition = Acquisition.builder().id(UUID.fromString(query.getAcquisitionId())).build();
            MarkRevoke markRevoke = markRevokeRepository.findByAcquisition(acquisition);

            MarkRevokeReply reply = MarkRevokeReply.builder()
                    .valid(true)
                    .acquisitionId(query.getAcquisitionId())
                    .sharingInformation(markRevoke == null ? "" : markRevoke.getSharingInformation())
                    .telesalesCommercialOffers(markRevoke == null ? "" : markRevoke.getTelesalesCommercialOffers())
                    .sendTextMessage(markRevoke == null ? "" : markRevoke.getSendTextMessage())
                    .sendingEmails(markRevoke == null ? "" : markRevoke.getSendingEmails())
                    .virtualExtracts(markRevoke == null ? "" : markRevoke.getVirtualExtracts())
                    .virtualCosts(markRevoke == null ? "" : markRevoke.getVirtualCosts())
                    .electronicMediaExtracts(markRevoke == null ? "" : markRevoke.getElectronicMediaExtracts())
                    .build();

            return reply;
        } catch (ValidationException | CustomException ex) {
            MarkRevokeReply reply = MarkRevokeReply.builder()
                    .valid(false)
                    .acquisitionId(query == null ? "" : query.getAcquisitionId())
                    .errorList(getErrorFromException(ex))
                    .build();

            return reply;
        }
    }

    private void validateMandatory(AcquisitionIdQuery query) {
        List<String> fieldList = new ArrayList<>();

        if (query == null) {
            fieldList.add("data");
        } else {
            if (query.getAcquisitionId() == null || query.getAcquisitionId().isEmpty()) {
                fieldList.add("acquisitionId");
            }
        }
        errorMandatory(fieldList);
    }
}
