package co.com.bancolombia.usecase.rabbit.signdocument;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SignDocumentReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;

public interface SignDocumentUseCase {
    SignDocumentReply signDocumentReply(NpGlobalServicesQuery query);
}
