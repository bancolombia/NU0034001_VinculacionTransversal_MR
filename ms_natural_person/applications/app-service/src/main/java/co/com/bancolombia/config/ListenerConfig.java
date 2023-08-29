package co.com.bancolombia.config;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SaveBasicInfoQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.SavePersonalInfoQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.query.UpdateCiiuQuery;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.InfoRutReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.SendFormReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.segmentcustomer.query.NpGlobalServicesQuery;
import co.com.bancolombia.model.economicinformation.gateways.EconomicRabbitRepository;
import co.com.bancolombia.model.exposerabbit.GlobalServicesRabbitRepository;
import co.com.bancolombia.model.exposerabbit.SaveBasicInfoRabbitRepository;
import co.com.bancolombia.model.exposerabbit.SendFormRabbitRepository;
import co.com.bancolombia.model.personalinformation.gateways.PersonalRabbitRepository;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityRabbitRepository;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_EXPOSE_SERVICES;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_REQUIRED_RUT;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_SAVE_BASIC_INFO;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_SAVE_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_SEND_FORM;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_UPDATE_CIIU;
import static co.com.bancolombia.commonsvnt.rabbit.RabbitConstants.RES_NP_VALIDATE_IDENTITY;

@Configuration
@RequiredArgsConstructor
public class ListenerConfig {

    private final EconomicRabbitRepository economicRabbitRepository;
    private final ValidateIdentityRabbitRepository validateIdentityRabbitRepository;
    private final GlobalServicesRabbitRepository globalServicesRabbitRepository;
    private final SaveBasicInfoRabbitRepository saveBasicInfoRabbitRepository;
    private final SendFormRabbitRepository sendFormRabbitRepository;
    private final PersonalRabbitRepository personalRabbitRepository;

    @Bean
    public HandlerRegistry handlerRegistry() {
        return HandlerRegistry.register()
                .serveQuery(RES_NP_REQUIRED_RUT, this::getRequiredRut, AcquisitionIdQuery.class)
                .serveQuery(RES_NP_VALIDATE_IDENTITY, this::validateIdentity, AcquisitionIdQuery.class)
                .serveQuery(RES_NP_EXPOSE_SERVICES, this::getToExposeServices, NpGlobalServicesQuery.class)
                .serveQuery(RES_NP_SAVE_BASIC_INFO, this::saveBasicInfo, SaveBasicInfoQuery.class)
                .serveQuery(RES_NP_SEND_FORM, this::sendForm, AcquisitionIdQuery.class)
                .serveQuery(RES_NP_UPDATE_CIIU, this::updateCiiu, UpdateCiiuQuery.class)
                .serveQuery(RES_NP_SAVE_PERSONAL_INFO, this::savePersonalInfo, SavePersonalInfoQuery.class);
    }

    public Mono<InfoRutReply> getRequiredRut(AcquisitionIdQuery query) {
        InfoRutReply reply = economicRabbitRepository.getRequiredRut(query);
        return Mono.just(reply);
    }

    public Mono<ValidateIdentityReply> validateIdentity(AcquisitionIdQuery query) {
        ValidateIdentityReply reply = validateIdentityRabbitRepository.validateIdentity(query);
        return Mono.just(reply);
    }

    public Mono<Object> getToExposeServices(NpGlobalServicesQuery query) {
        return Mono.just(globalServicesRabbitRepository.servicesReply(query));
    }

    public Mono<EmptyReply> saveBasicInfo(SaveBasicInfoQuery query) {
        return Mono.just(saveBasicInfoRabbitRepository.saveResult(query));
    }

    public Mono<SendFormReply> sendForm(AcquisitionIdQuery query) {
        SendFormReply reply = sendFormRabbitRepository.sendFormReply(query);
        return Mono.just(reply);
    }

    public Mono<EmptyReply> updateCiiu(UpdateCiiuQuery query) {
        EmptyReply reply = economicRabbitRepository.updateCiiu(query);
        return Mono.just(reply);
    }

    public  Mono<EmptyReply> savePersonalInfo(SavePersonalInfoQuery query){
        EmptyReply reply = personalRabbitRepository.saveResult(query);
        return  Mono.just(reply);
    }
}