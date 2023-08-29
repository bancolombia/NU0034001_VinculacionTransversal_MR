package co.com.bancolombia.usecase.rabbit.genandexpdoc;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.GenExposeReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;

public interface GenAndExpDocUseCase {
    GenExposeReply genAndExpDocUseCaseOneReply(NpGlobalServicesQuery query);
}