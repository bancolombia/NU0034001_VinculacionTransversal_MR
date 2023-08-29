package co.com.bancolombia.config.rabbitmq;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.query.ParameterQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.ParameterReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.query.ControlListSaveQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.ControlListSaveReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
import co.com.bancolombia.model.controllist.gateways.ControlListSaveRabbitRepository;
import co.com.bancolombia.model.exposerabbit.IdentityValResultRabbitRepository;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRabbitRepository;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_RI_CODE_PARAMETER;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_RI_STATE_CONTROL_LIST;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_RI_VALIDATION_IDENTITY_RESULT;

@Configuration
@RequiredArgsConstructor
public class ListenerConfig {

    private final ControlListSaveRabbitRepository controlListSaveRabbitRepository;
    private final IdentityValResultRabbitRepository iValResultRabbitRepository;
    private final ParametersRabbitRepository parametersRabbitRepository;

    @Bean
    public HandlerRegistry handlerRegistry() {
        return HandlerRegistry.register()
                .serveQuery(RES_RI_STATE_CONTROL_LIST, this::stateControlList, ControlListSaveQuery.class)
                .serveQuery(RES_RI_VALIDATION_IDENTITY_RESULT, this::getIndentityValResultReply,
                        AcquisitionIdQuery.class)
                .serveQuery(RES_RI_CODE_PARAMETER, this::findParameterByNameParent, ParameterQuery.class);
    }

    public Mono<ControlListSaveReply> stateControlList(ControlListSaveQuery query) {
        ControlListSaveReply reply = controlListSaveRabbitRepository.findStateValidationCustomerControlList(query);
        return Mono.just(reply);
    }

    public Mono<IdentityValResultReply> getIndentityValResultReply(AcquisitionIdQuery query) {
        IdentityValResultReply reply = iValResultRabbitRepository.getIndentityValResultReply(query);
        return Mono.just(reply);
    }

    public Mono<ParameterReply> findParameterByNameParent(ParameterQuery query) {
        ParameterReply reply = parametersRabbitRepository.findByNameParent(query);
        return Mono.just(reply);
    }

}
