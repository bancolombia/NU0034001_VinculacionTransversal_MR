package co.com.bancolombia.model.sqs.gateways;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.sqs.SqsMessage;

import java.util.List;
import java.util.UUID;

public interface SqsMessageRepository {
    public SqsMessage save(SqsMessage sqsMessage);

    List<SqsMessage> findByOperationAndAcquisitionOrderByCreatedDateDesc(String operation, Acquisition acquisition);

    SqsMessage findTopByOperationAndAcquisitionOrderByCreatedDateDesc(String operation, Acquisition acquisition);

    SqsMessage findById(UUID id);

    boolean deleteSqsMessage(SqsMessage sqsMessage);
}
