package co.com.bancolombia.config;


import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.controllist.ControlListTransformUseCase;
import co.com.bancolombia.controllist.ControlListTransformUseCaseImpl;
import co.com.bancolombia.controllist.ControlListUseCase;
import co.com.bancolombia.controllist.ControlListUseCaseImpl;
import co.com.bancolombia.expoquestion.ExpoQuestionIdentificationUseCase;
import co.com.bancolombia.expoquestion.ExpoQuestionIdentificationUseCaseImpl;
import co.com.bancolombia.expoquestion.ExpoQuestionQuestionnaireTransUseCase;
import co.com.bancolombia.expoquestion.ExpoQuestionQuestionnaireTransUseCaseImpl;
import co.com.bancolombia.expoquestion.ExpoQuestionQuestionnaireUseCase;
import co.com.bancolombia.expoquestion.ExpoQuestionQuestionnaireUseCaseImpl;
import co.com.bancolombia.expoquestion.ExpoQuestionSaveUseCase;
import co.com.bancolombia.expoquestion.ExpoQuestionSaveUseCaseImpl;
import co.com.bancolombia.expoquestion.ExpoQuestionTransUseCase;
import co.com.bancolombia.expoquestion.ExpoQuestionTransUseCaseImpl;
import co.com.bancolombia.expoquestion.ExpoQuestionValidationUseCase;
import co.com.bancolombia.expoquestion.ExpoQuestionValidationUseCaseImpl;
import co.com.bancolombia.markcustomer.MarkCustomerSaveUseCase;
import co.com.bancolombia.markcustomer.MarkCustomerSaveUseCaseImpl;
import co.com.bancolombia.markcustomer.MarkCustomerUseCase;
import co.com.bancolombia.markcustomer.MarkCustomerUseCaseImpl;
import co.com.bancolombia.markcustomer.MarkCustomerValidationsUseCase;
import co.com.bancolombia.markcustomer.MarkCustomerValidationsUseCaseImpl;
import co.com.bancolombia.model.commons.ApiRiskCredential;
import co.com.bancolombia.model.controllist.gateways.ControlListRestRepository;
import co.com.bancolombia.model.controllist.gateways.ControlListSaveRabbitRepository;
import co.com.bancolombia.model.controllist.gateways.ControlListSaveRepository;
import co.com.bancolombia.model.expoquestion.gateways.ExpoQuestionSaveRepository;
import co.com.bancolombia.model.expoquestion.gateways.QuestionnaireRestRepository;
import co.com.bancolombia.model.expoquestion.gateways.ValidateIdentificationRestRepository;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRepository;
import co.com.bancolombia.model.markcustomer.gateways.RegisterControlListRestRepository;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityRestRepository;
import co.com.bancolombia.model.validateidentity.gateways.ValidateIdentityScoreRepository;
import co.com.bancolombia.model.validateidentity.gateways.ValidateSaveRepository;
import co.com.bancolombia.model.validatequestion.gateways.QuestionnaireVerifyRestRepository;
import co.com.bancolombia.model.validatequestion.gateways.ValidateQuestionSaveRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.NaturalPersonUseCaseImpl;
import co.com.bancolombia.rabbit.TriggerExceptionUseCase;
import co.com.bancolombia.rabbit.TriggerExceptionUseCaseImpl;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUtilUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUtilUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentitySaveUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentitySaveUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityTransformUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityTransformUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityValidationRuleUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityValidationRuleUseCaseImpl;
import co.com.bancolombia.validatequestion.ValidateQuestionSaveUseCase;
import co.com.bancolombia.validatequestion.ValidateQuestionSaveUseCaseImpl;
import co.com.bancolombia.validatequestion.ValidateQuestionUseCase;
import co.com.bancolombia.validatequestion.ValidateQuestionUseCaseImpl;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public CoreFunctionDate createModuleCoreFunctionDate() {
        return new CoreFunctionDate();
    }

    @Bean
    public TriggerExceptionUseCase createModuleTriggerExceptionUseCase() {
        return new TriggerExceptionUseCaseImpl();
    }

    @Bean
    public VinculationUpdateUseCase vinculationUpdateUseCase(DirectAsyncGateway directAsyncGateway,
                                                             TriggerExceptionUseCase triggerExceptionUseCase) {
        return new VinculationUpdateUseCaseImpl(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public NaturalPersonUseCase createModuleNaturalPersonUseCase(DirectAsyncGateway directAsyncGateway,
                                                                 TriggerExceptionUseCase triggerExceptionUseCase) {
        return new NaturalPersonUseCaseImpl(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public ValidateIdentityRuleUseCase createModuleValidateIdentityRuleUseCase(ValidateIdentityRuleUtilUseCase validateIRUtilUC,
                                                                               VinculationUpdateUseCase vinculationUpdateUseCase,
                                                                               ValidateIdentityValidationRuleUseCase vIValidationRuleUC,
                                                                               ValidateIdentityScoreRepository scoreRepository) {
        return new ValidateIdentityRuleUseCaseImpl(validateIRUtilUC, vinculationUpdateUseCase, vIValidationRuleUC,
                scoreRepository);
    }

    @Bean
    public ValidateIdentityRuleUtilUseCase createModuleValidateIdentityRuleUtilUseCase(ParametersRepository parametersRepository,
                                                                                       VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new ValidateIdentityRuleUtilUseCaseImpl(parametersRepository, vinculationUpdateUseCase);
    }

    @Bean
    public ValidateIdentitySaveUseCase createModuleValidateIdentitySaveUseCase(ValidateIdentityTransformUseCase vIdentityTUseCase,
                                                                               ValidateSaveRepository saveRepository) {
        return new ValidateIdentitySaveUseCaseImpl(vIdentityTUseCase, saveRepository);
    }

    @Bean
    public ValidateIdentityTransformUseCase createValidateIdentityTransformUseCase(CoreFunctionDate coreFD) {
        return new ValidateIdentityTransformUseCaseImpl(coreFD);
    }

    @Bean
    public ValidateIdentityUseCase createModuleValidateIdentityUseCase(NaturalPersonUseCase naturalUseCase,
                                                                       ValidateIdentityRestRepository validateIdentityRestRepository,
                                                                       CoreFunctionDate coreFunctionDate,
                                                                       VinculationUpdateUseCase vinculationUpdateUseCase,
                                                                       ValidateIdentityRuleUseCase vIdentityRuleUseCase,
                                                                       ValidateIdentitySaveUseCase identitySaveUseCase) {
        return new ValidateIdentityUseCaseImpl(naturalUseCase, validateIdentityRestRepository, coreFunctionDate,
                vinculationUpdateUseCase, vIdentityRuleUseCase, identitySaveUseCase);
    }

    @Bean
    public ValidateIdentityValidationRuleUseCase createModuleIdentityValidationRuleUseCase(CoreFunctionDate coreFunctionDate,
                                                                                           ValidateIdentityRuleUtilUseCase validateIUtil) {
        return new ValidateIdentityValidationRuleUseCaseImpl(coreFunctionDate, validateIUtil);
    }

    @Bean
    public ExpoQuestionIdentificationUseCase createModuleExpoQuestionIdentificationUseCase(ExpoQuestionSaveUseCase expoQuestionSUC,
                                                                                           ValidateIdentificationRestRepository vIdentificationRestRepository,
                                                                                           VinculationUpdateUseCase vinculationUpdateUseCase,
                                                                                           ExpoQuestionQuestionnaireUseCase questionnaireUC,
                                                                                           ExpoQuestionValidationUseCase expoQuestionUC,
                                                                                           ApiRiskCredential apiRiskCredential) {
        return new ExpoQuestionIdentificationUseCaseImpl(vIdentificationRestRepository, vinculationUpdateUseCase,
                questionnaireUC, expoQuestionSUC, expoQuestionUC, apiRiskCredential);
    }

    @Bean
    public ExpoQuestionQuestionnaireTransUseCase createModuleExpoQuestionQuestionnaireTransUseCase(CoreFunctionDate coreFD) {
        return new ExpoQuestionQuestionnaireTransUseCaseImpl(coreFD);
    }

    @Bean
    public ExpoQuestionQuestionnaireUseCase createModuleExpoQuestionQuestionnaireUseCase(
            CoreFunctionDate coreFD,
            ExpoQuestionSaveUseCase expoQuestionSUC,
            QuestionnaireRestRepository questionnaireRestRepository,
            VinculationUpdateUseCase vinculationUpdateUseCase,
            ExpoQuestionValidationUseCase expoQuestionUC,
            ApiRiskCredential apiRiskCredential) {
        return new ExpoQuestionQuestionnaireUseCaseImpl(expoQuestionSUC, coreFD, questionnaireRestRepository,
                vinculationUpdateUseCase, expoQuestionUC, apiRiskCredential);
    }

    @Bean
    public ExpoQuestionSaveUseCase createModuleExpoQuestionSaveUseCase(ExpoQuestionSaveRepository saveRepository,
                                                                       ExpoQuestionTransUseCase expoQuestionTUC,
                                                                       ExpoQuestionQuestionnaireTransUseCase questionnaireTUC) {
        return new ExpoQuestionSaveUseCaseImpl(saveRepository, expoQuestionTUC, questionnaireTUC);
    }

    @Bean
    public ExpoQuestionTransUseCase createModuleExpoQuestionTransUseCase(CoreFunctionDate coreFD) {
        return new ExpoQuestionTransUseCaseImpl(coreFD);
    }

    @Bean
    public ExpoQuestionValidationUseCase createModuleExpoQuestionValidationUseCase(CoreFunctionDate coreFD,
                                                                                   VinculationUpdateUseCase vinculationUpdateUseCase,
                                                                                   NaturalPersonUseCase naturalPersonUseCase) {
        return new ExpoQuestionValidationUseCaseImpl(vinculationUpdateUseCase, naturalPersonUseCase, coreFD);
    }

    @Bean
    public ControlListUseCase createModuleControlListUseCase(ControlListRestRepository controlListRestRepository,
                                                             NaturalPersonUseCase naturalUseCase,
                                                             CoreFunctionDate coreFunctionDate,
                                                             ControlListSaveRepository controlSaveRepository,
                                                             ControlListTransformUseCase controlListTransform) {
        return new ControlListUseCaseImpl(controlListRestRepository, naturalUseCase, coreFunctionDate,
                controlSaveRepository, controlListTransform);
    }

    @Bean
    public ControlListTransformUseCase createModuleControlListTransformUseCase(VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new ControlListTransformUseCaseImpl(vinculationUpdateUseCase);
    }

    @Bean
    public ValidateQuestionSaveUseCase createModuleValidateQuestionSaveUseCase(ValidateQuestionSaveRepository validateQuestionSaveRepository,
                                                                               CoreFunctionDate coreFD) {
        return new ValidateQuestionSaveUseCaseImpl(validateQuestionSaveRepository, coreFD);
    }

    @Bean
    public ValidateQuestionUseCase createModuleValidateQuestionUseCase(QuestionnaireVerifyRestRepository qRestRepository,
                                                                       ValidateQuestionSaveUseCase questionSaveUseCase,
                                                                       VinculationUpdateUseCase vinculationUpdateUseCase,
                                                                       CoreFunctionDate coreFunctionDate,
                                                                       ApiRiskCredential apiRiskCredential) {
        return new ValidateQuestionUseCaseImpl(qRestRepository, questionSaveUseCase, vinculationUpdateUseCase,
                coreFunctionDate, apiRiskCredential);
    }

    @Bean
    public MarkCustomerSaveUseCase markCustomerSaveUseCase(RegisterControlListRepository repository, CoreFunctionDate coreFD) {
        return new MarkCustomerSaveUseCaseImpl(repository, coreFD);
    }

    @Bean
    public MarkCustomerUseCase markCustomerUseCase (RegisterControlListRestRepository restRepository,
            MarkCustomerSaveUseCase markCustomerSaveUseCase, CoreFunctionDate coreFunctionDate,
            MarkCustomerValidationsUseCase markValidations, VinculationUpdateUseCase vinculationUpdateUseCase,
            NaturalPersonUseCase naturalPersonUseCase) {
        return new MarkCustomerUseCaseImpl(restRepository, markCustomerSaveUseCase, coreFunctionDate, markValidations, vinculationUpdateUseCase, naturalPersonUseCase);
    }

    @Bean
    public MarkCustomerValidationsUseCase markCustomerValidationsUseCase (VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new MarkCustomerValidationsUseCaseImpl(vinculationUpdateUseCase);
    }

}

