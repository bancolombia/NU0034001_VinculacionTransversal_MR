package co.com.bancolombia.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.model.exposerabbit.GlobalServicesRabbitRepository;
import co.com.bancolombia.usecase.rabbit.genandexpdoc.GenAndExpDocUseCase;
import co.com.bancolombia.usecase.rabbit.segmentcustomer.SegmentCustomerInitialUseCase;
import co.com.bancolombia.usecase.rabbit.signdocument.SignDocumentUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GEN_EXP_DOCS;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SEGMENT_CUSTOMER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_SIGNDOCUMENT;

@Component
@AllArgsConstructor
public class GlobalServicesRabbit extends ErrorHandleRabbit implements GlobalServicesRabbitRepository {

    private final SegmentCustomerInitialUseCase segmentCustomerUseCase;
    private final GenAndExpDocUseCase genAndExpDocUseCase;
    private final SignDocumentUseCase signDocumentUseCase;

    @Override
    public Object servicesReply(NpGlobalServicesQuery query) {
        Object reply = null;
        if (query.getOperation().equals(CODE_SEGMENT_CUSTOMER)){
            reply = segmentCustomerUseCase.segmentCustomerReply(query);
        }
        if (query.getOperation().equals(CODE_GEN_EXP_DOCS)){
            reply = genAndExpDocUseCase.genAndExpDocUseCaseOneReply(query);
        }
        if(query.getOperation().equals(CODE_SIGNDOCUMENT)){
            reply = signDocumentUseCase.signDocumentReply(query);
        }
        return reply;
    }
}