package co.com.bancolombia.config;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.model.basicinformation.gateways.BasicInformationRepository;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.economicinformation.gateways.EconomicInformationRepository;
import co.com.bancolombia.model.foreigninformation.gateways.ForeignInformationRepository;
import co.com.bancolombia.model.foreigninformationcurrency.gateways.ForeignInformationCurrencyRepository;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRepository;
import co.com.bancolombia.model.generatetoken.gateways.GenerateTokenRestRepository;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.personalinformation.gateways.PersonalInformationRepository;
import co.com.bancolombia.model.taxcountry.gateways.TaxCountryRepository;
import co.com.bancolombia.model.tokenretries.gateways.TokenRetriesRepository;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRepository;
import co.com.bancolombia.model.validatetoken.gateways.ValidateTokenRestRepository;
import co.com.bancolombia.taxinformation.gateways.TaxInformationRepository;
import co.com.bancolombia.usecase.basicinformation.BasicInformationUseCase;
import co.com.bancolombia.usecase.basicinformation.ValidateCatalogsBasicUseCase;
import co.com.bancolombia.usecase.contactinformation.ContactIValidationUseCase;
import co.com.bancolombia.usecase.contactinformation.ContactInformationProcessUseCase;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCase;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCaseImpl;
import co.com.bancolombia.usecase.contactinformation.ContactInformationUseCasePersist;
import co.com.bancolombia.usecase.contactinformation.ContactValidationUseCaseImpl;
import co.com.bancolombia.usecase.contactinformation.ValidateCatalogsContactUseCase;
import co.com.bancolombia.usecase.dependentfield.DependentFieldUseCase;
import co.com.bancolombia.usecase.economicinformation.EconomicInformationUseCase;
import co.com.bancolombia.usecase.economicinformation.EconomicInformationUseCaseImpl;
import co.com.bancolombia.usecase.economicinformation.ValidateCatalogsEconomicUseCase;
import co.com.bancolombia.usecase.foreigninformation.ForeignInformationUseCase;
import co.com.bancolombia.usecase.foreigninformation.ForeignInformationUseCaseImpl;
import co.com.bancolombia.usecase.foreigninformation.ValidateCatalogsForeignUseCase;
import co.com.bancolombia.usecase.foreigninformationcurrency.ForeignInformationCurrencyUseCase;
import co.com.bancolombia.usecase.foreigninformationcurrency.ForeignInformationCurrencyUseCaseImpl;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenConstructUseCase;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenConstructUseCaseImpl;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenUseCase;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenUseCaseImpl;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenValidationUseCase;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenValidationUseCaseImpl;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.merge.MergeUseCaseImpl;
import co.com.bancolombia.usecase.parameters.ParametersUseCase;
import co.com.bancolombia.usecase.parameters.ParametersUseCaseImpl;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationStartProcessUseCase;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationStartProcessUseCaseImpl;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCaseImpl;
import co.com.bancolombia.usecase.personalinformation.ValidateCatalogsPersonalUseCase;
import co.com.bancolombia.usecase.rabbit.genandexpdoc.GenAndExpDocUseCase;
import co.com.bancolombia.usecase.rabbit.genandexpdoc.GenAndExpDocUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.genandexpdoc.GenAndExpDocUseCaseUtilThree;
import co.com.bancolombia.usecase.rabbit.genandexpdoc.GenAndExpDocUseCaseUtilTwo;
import co.com.bancolombia.usecase.rabbit.segmentcustomer.SegmentCustomerFinalUseCase;
import co.com.bancolombia.usecase.rabbit.segmentcustomer.SegmentCustomerInitialUseCase;
import co.com.bancolombia.usecase.rabbit.signdocument.SignDocumentUseCase;
import co.com.bancolombia.usecase.rabbit.signdocument.SignDocumentUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.signdocument.SignDocumentUseCaseUtil;
import co.com.bancolombia.usecase.rabbit.util.ConstructResponsesUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.TriggerExceptionUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseImpl;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCaseTwo;
import co.com.bancolombia.usecase.taxcountry.TaxCountryUseCase;
import co.com.bancolombia.usecase.taxcountry.TaxCountryUseCaseImpl;
import co.com.bancolombia.usecase.taxinformation.TaxInformationUseCase;
import co.com.bancolombia.usecase.taxinformation.TaxInformationUseCaseImpl;
import co.com.bancolombia.usecase.taxinformation.ValidateCatalogsTaxUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCase;
import co.com.bancolombia.usecase.tokenretries.TokenRetriesUseCaseImpl;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import co.com.bancolombia.usecase.util.ValidateMandatoryFields;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenCreateRequestUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenMapperUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenMapperUseCaseImpl;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenUseCase;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenUseCaseImpl;
import co.com.bancolombia.usecase.validatetoken.ValidateTokenValidationUseCase;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UseCasesConfig {

    @Bean
    public CoreFunctionDate createModuleCoreFunctionDate() {
        return new CoreFunctionDate();
    }

    @Bean
    public CoreFunctionString createModuleCoreFunctionString() {
        return new CoreFunctionString();
    }

    @Bean
    public Exceptions exceptions() {
        return new Exceptions();
    }

    @Bean
    public MergeUseCase mergeUseCase(VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new MergeUseCaseImpl(vinculationUpdateUseCase);
    }

    @Bean
    public TriggerExceptionUseCase triggerExceptionUseCase() {
        return new TriggerExceptionUseCaseImpl();
    }

    @Bean
    public VinculationUpdateUseCase vinculationUpdateUseCase(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase,
            VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo) {
        return new VinculationUpdateUseCaseImpl(directAsyncGateway, triggerExceptionUseCase,
                vinculationUpdateUseCaseTwo);
    }

    @Bean
    public VinculationUpdateUseCaseTwo vinculationUpdateUseCaseTwo(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase) {
        return new VinculationUpdateUseCaseTwo(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public ValidateMandatoryFields checklistValidateUseCase() {
        return new ValidateMandatoryFields();
    }

    @Bean
    public ParametersUseCase parametersUseCase(ParametersRepository parametersRepository) {
        return new ParametersUseCaseImpl(parametersRepository);
    }

    @Bean
    public DependentFieldUseCase dependentFieldUseCase(
            VinculationUpdateUseCase vinculationUpdateUseCase, TaxInformationRepository taxInformationRepository) {
        return new DependentFieldUseCase(vinculationUpdateUseCase, taxInformationRepository);
    }

    @Bean
    public UtilCatalogs utilCatalogs(VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new UtilCatalogs(vinculationUpdateUseCase);
    }

    @Bean
    public ValidateCatalogsBasicUseCase basicInfoVCUseCase(UtilCatalogs utilCatalogs) {
        return new ValidateCatalogsBasicUseCase(utilCatalogs);
    }

    @Bean
    public ValidateCatalogsEconomicUseCase catalogInfoUseCase(UtilCatalogs utilCatalogs) {
        return new ValidateCatalogsEconomicUseCase(utilCatalogs);
    }

    @Bean
    public ValidateCatalogsForeignUseCase foreignInfoVCUseCase(UtilCatalogs utilCatalogs) {
        return new ValidateCatalogsForeignUseCase(utilCatalogs);
    }

    @Bean
    public ValidateCatalogsPersonalUseCase personalInfoVCUseCase(UtilCatalogs utilCatalogs) {
        return new ValidateCatalogsPersonalUseCase(utilCatalogs);
    }

    @Bean
    public ValidateCatalogsContactUseCase contactInfoVCUseCase(UtilCatalogs utilCatalogs) {
        return new ValidateCatalogsContactUseCase(utilCatalogs);
    }

    @Bean
    public ValidateCatalogsTaxUseCase taxInfoVCUseCase(
            VinculationUpdateUseCase vUpdate, UtilCatalogs utilCatalogs, CoreFunctionString coreFunctionString) {
        return new ValidateCatalogsTaxUseCase(vUpdate, utilCatalogs, coreFunctionString);
    }

    @Bean
    public ConstructResponsesUseCase constructResponsesUseCase() {
        return new ConstructResponsesUseCase();
    }

    @Bean
    public BasicInformationUseCase basicInformationUseCase(
            VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsBasicUseCase validateCatalogsBasicUseCase,
            ValidateMandatoryFields checklistValidateMandatoryFields,
            MergeUseCase mergeUseCase, BasicInformationRepository basicInformationRepository) {
        return new BasicInformationUseCase(vinculationUpdateUseCase, validateCatalogsBasicUseCase,
                checklistValidateMandatoryFields, mergeUseCase, basicInformationRepository);
    }

    @Bean
    public ForeignInformationCurrencyUseCase foreignInformationCurrencyUseCase(
            DependentFieldUseCase dependentFieldUseCase, MergeUseCase mergeUseCase,
            ForeignInformationCurrencyRepository repository) {
        return new ForeignInformationCurrencyUseCaseImpl(
                dependentFieldUseCase, mergeUseCase, repository);
    }

    @Bean
    public ForeignInformationUseCase foreignInformationUseCase(
            DependentFieldUseCase dependentFieldUseCase,
            VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsForeignUseCase validateCatalogsForeignUseCase,
            MergeUseCase mergeUseCase, @Lazy ForeignInformationCurrencyUseCaseImpl foreignInformationCurrencyUseCase,
            ForeignInformationRepository repository) {
        return new ForeignInformationUseCaseImpl(
                dependentFieldUseCase, vinculationUpdateUseCase, validateCatalogsForeignUseCase,
                mergeUseCase, foreignInformationCurrencyUseCase, repository);
    }

    @Bean
    public ContactInformationProcessUseCase contactInformationProcessUseCase(
            DependentFieldUseCase dependentFieldUseCase, ContactInformationUseCasePersist cInfoUCPer,
            VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsContactUseCase validateCatalogsContactUseCase) {
        return new ContactInformationProcessUseCase(dependentFieldUseCase, cInfoUCPer,
                vinculationUpdateUseCase, validateCatalogsContactUseCase);
    }

    @Bean
    public ContactInformationUseCase contactInformationUseCase(
            MergeUseCase mergeUseCase, @Lazy PersonalInformationUseCase perInfoUseCase,
            ContactInformationUseCasePersist cInfoUCPer, ContactInformationProcessUseCase cInfoProcUseCase,
            VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new ContactInformationUseCaseImpl(mergeUseCase, perInfoUseCase,
                cInfoUCPer, cInfoProcUseCase, vinculationUpdateUseCase);
    }

    @Bean
    public ContactIValidationUseCase contactIValidationUseCase(
            DependentFieldUseCase dependentFieldUseCase, ContactInformationRepository contactInformationRepository,
            ParametersUseCase parametersUseCase, VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new ContactValidationUseCaseImpl(
                dependentFieldUseCase, contactInformationRepository, parametersUseCase, vinculationUpdateUseCase);
    }

    @Bean
    public ContactInformationUseCasePersist contactInformationUseCasePersist(
            ContactInformationRepository contactInformationRepository, ContactIValidationUseCase cValidationUseCase) {
        return new ContactInformationUseCasePersist(contactInformationRepository, cValidationUseCase);
    }

    @Bean(name = "PersonalInformationUseCase")
    public PersonalInformationUseCase createModulePersonaInformation(
            PersonalInformationRepository repository, ContactInformationUseCase contactInformationUseCase,
            ParametersUseCase parametersUseCase, @Lazy ValidateMandatoryFields validateMandatory,
            VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsPersonalUseCase validateCatalogsPersonalUseCase) {
        return new PersonalInformationUseCaseImpl(repository, contactInformationUseCase, parametersUseCase,
                validateMandatory, vinculationUpdateUseCase, validateCatalogsPersonalUseCase);
    }

    @Bean
    public PersonalInformationStartProcessUseCase createModulePersonaInformationStartProcess(
            PersonalInformationUseCase personalInformationUseCase, PersonalInformationRepository repository,
            MergeUseCase mergeUseCase) {
        return new PersonalInformationStartProcessUseCaseImpl(personalInformationUseCase,
                repository, mergeUseCase);
    }

    @Bean
    public EconomicInformationUseCase economicInformationUseCase(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsEconomicUseCase validateCatalogsEconomicUseCase, MergeUseCase mergeUseCase,
            ParametersUseCase parametersUseCase,
            EconomicInformationRepository economicInformationRepository) {
        return new EconomicInformationUseCaseImpl(dependentFieldUseCase,
                vinculationUpdateUseCase, validateCatalogsEconomicUseCase, mergeUseCase,
                parametersUseCase, economicInformationRepository);
    }

    @Bean
    public TaxCountryUseCase taxCountryUseCase(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            TaxCountryRepository taxCountryRepository, MergeUseCase mergeUseCase) {
        return new TaxCountryUseCaseImpl(dependentFieldUseCase, vinculationUpdateUseCase,
                taxCountryRepository, mergeUseCase);
    }

    @Bean
    public TaxInformationUseCase taxInformationUseCase(
            DependentFieldUseCase dependentFieldUseCase, VinculationUpdateUseCase vinculationUpdateUseCase,
            ValidateCatalogsTaxUseCase validateCatalogsTaxUseCase, TaxInformationRepository taxInformationRepository,
            TaxCountryUseCase taxCountryUseCase, MergeUseCase mergeUseCase) {
        return new TaxInformationUseCaseImpl(dependentFieldUseCase, vinculationUpdateUseCase,
                validateCatalogsTaxUseCase,
                taxInformationRepository, taxCountryUseCase, mergeUseCase);
    }

    @Bean
    public SegmentCustomerFinalUseCase segmentCustomerFinalUseCase(
            ConstructResponsesUseCase constructResponsesUseCase, TaxInformationRepository taxInformationRepository,
            TaxCountryRepository taxCountryRepository, ForeignInformationRepository foreignInformationRepository,
            ForeignInformationCurrencyRepository foreignInformationCurrencyRepository) {
        return new SegmentCustomerFinalUseCase(constructResponsesUseCase, taxInformationRepository,
                taxCountryRepository, foreignInformationRepository, foreignInformationCurrencyRepository);
    }

    @Bean
    public SegmentCustomerInitialUseCase segmentCustomerInitialUseCase(
            ConstructResponsesUseCase construct, BasicInformationUseCase bInfo, PersonalInformationRepository pInfo,
            ContactInformationRepository cInfo, EconomicInformationRepository eInfo,
            SegmentCustomerFinalUseCase segmentCustomerFinalUseCase) {
        return new SegmentCustomerInitialUseCase(construct, bInfo, pInfo, cInfo, eInfo, segmentCustomerFinalUseCase);
    }

    @Bean
    public GenAndExpDocUseCase genAndExpDocUseCase(
            ConstructResponsesUseCase construct, ContactInformationRepository contactInformationRepository,
            EconomicInformationRepository economicInformationRepository,
            GenAndExpDocUseCaseUtilTwo genAndExpDocUseCaseUtilTwo) {
        return new GenAndExpDocUseCaseImpl(construct, contactInformationRepository,
                economicInformationRepository, genAndExpDocUseCaseUtilTwo);
    }

    @Bean
    public GenAndExpDocUseCaseUtilTwo genAndExpDocUseCaseUtilTwo(
            ConstructResponsesUseCase constructReplyGenAndDoc, BasicInformationUseCase basicInformationUseCase,
            PersonalInformationRepository personalInformationRepository,
            GenAndExpDocUseCaseUtilThree genAndExpDocUseCaseUtilThree) {
        return new GenAndExpDocUseCaseUtilTwo(constructReplyGenAndDoc, basicInformationUseCase,
                personalInformationRepository, genAndExpDocUseCaseUtilThree);
    }

    @Bean
    public GenAndExpDocUseCaseUtilThree genAndExpDocUseCaseUtilThree(
            ConstructResponsesUseCase constructReplyGenAndDoc, TaxInformationRepository taxInformationRepository,
            TaxCountryRepository taxCountryRepository, ForeignInformationRepository foreignInformationRepository,
            ForeignInformationCurrencyRepository foreignInformationCurrencyRepository) {
        return new GenAndExpDocUseCaseUtilThree(constructReplyGenAndDoc, taxInformationRepository,
                taxCountryRepository, foreignInformationRepository, foreignInformationCurrencyRepository);
    }

    @Bean
    public TokenRetriesUseCase tokenRetriesUseCase(TokenRetriesRepository tokenRetriesRepository,
                                                   CoreFunctionDate coreFunctionDate) {
        return new TokenRetriesUseCaseImpl(tokenRetriesRepository, coreFunctionDate);
    }

    @Bean
    public GenerateTokenValidationUseCase generateTokenValidationUseCase(VinculationUpdateUseCase
                                                                                 vinculationUpdateUseCase,
                                                                         Exceptions exceptions,
                                                                         ParametersRepository parametersRepository,
                                                                         CoreFunctionDate coreFunctionDate,
                                                                         TokenRetriesUseCase tokenRetriesUseCase) {
        return new GenerateTokenValidationUseCaseImpl(vinculationUpdateUseCase, exceptions, parametersRepository,
                coreFunctionDate, tokenRetriesUseCase);
    }

    @Bean
    public GenerateTokenConstructUseCase generateTokenConstructUseCase(CoreFunctionDate coreFunctionDate) {
        return new GenerateTokenConstructUseCaseImpl(coreFunctionDate);
    }

    @Bean
    public GenerateTokenUseCase generateTokenUseCase(VinculationUpdateUseCase vinculationUpdateUseCase,
                                                     GenerateTokenRepository generateTokenRepository,
                                                     PersonalInformationRepository personalInformationRepository,
                                                     GenerateTokenValidationUseCase generateTokenValidationUseCase,
                                                     GenerateTokenRestRepository generateTokenRestRepository,
                                                     CoreFunctionDate coreFunctionDate,
                                                     TokenRetriesUseCase tokenRetriesUseCase,
                                                     GenerateTokenConstructUseCase generateTokenConstructUseCase) {
        return new GenerateTokenUseCaseImpl(vinculationUpdateUseCase, generateTokenRepository,
                personalInformationRepository, generateTokenValidationUseCase, generateTokenRestRepository,
                coreFunctionDate, tokenRetriesUseCase, generateTokenConstructUseCase);
    }

    @Bean
    public ValidateTokenMapperUseCase validateTokenMapperUseCase(CoreFunctionDate coreFunctionDate) {
        return new ValidateTokenMapperUseCaseImpl(coreFunctionDate);
    }

    @Bean
    public ValidateTokenCreateRequestUseCase validateTokenCreateRequestUseCase() {
        return new ValidateTokenCreateRequestUseCase();
    }

    @Bean
    public ValidateTokenValidationUseCase validateTokenValidationUseCase(VinculationUpdateUseCase vinculationUpdateUseCase,
                                                                         CoreFunctionDate coreFunctionDate,
                                                                         TokenRetriesUseCase tokenRetriesUseCase,
                                                                         ParametersUseCase parametersUseCase,
                                                                         Exceptions exceptions) {
        return new ValidateTokenValidationUseCase(vinculationUpdateUseCase, coreFunctionDate, tokenRetriesUseCase,
                parametersUseCase, exceptions);
    }

    @Bean
    public ValidateTokenUseCase validateTokenUseCase(ValidateTokenRepository validateTokenRepository,
                                                     ValidateTokenMapperUseCase validateTokenMapperUseCase,
                                                     ValidateTokenRestRepository validateTokenRestRepository,
                                                     GenerateTokenUseCase generateTokenUseCase,
                                                     VinculationUpdateUseCase vinculationUpdateUseCase,
                                                     TokenRetriesUseCase tokenRetriesUseCase,
                                                     CoreFunctionDate coreFunctionDate,
                                                     ValidateTokenValidationUseCase validateTokenValidationUseCase,
                                                     ValidateTokenCreateRequestUseCase createRequest) {
        return new ValidateTokenUseCaseImpl(validateTokenRepository, validateTokenMapperUseCase,
                validateTokenRestRepository, generateTokenUseCase, vinculationUpdateUseCase, tokenRetriesUseCase,
                coreFunctionDate, validateTokenValidationUseCase, createRequest);
    }

    @Bean
    public SignDocumentUseCase signDocumentUseCase(
            ConstructResponsesUseCase consResponseUseCase, ValidateTokenUseCase validateTokenUseCase,
            BasicInformationUseCase basicInfoUseCase, PersonalInformationRepository personalInfoRepository,
            EconomicInformationRepository economicInfoRepository, SignDocumentUseCaseUtil signDocumentUseCaseUtil) {
        return new SignDocumentUseCaseImpl(consResponseUseCase, validateTokenUseCase, basicInfoUseCase,
                personalInfoRepository, economicInfoRepository, signDocumentUseCaseUtil);
    }

    @Bean
    public SignDocumentUseCaseUtil signDocumentUseCaseUtil(
            ContactInformationRepository contactInformationRepository,
            TaxInformationRepository taxInformationRepository, TaxCountryRepository taxCountryRepository,
            ForeignInformationRepository foreignInformationRepository,
            ForeignInformationCurrencyRepository foreignInformationCurrencyRepository,
            ConstructResponsesUseCase constructResponsesUseCase) {
        return new SignDocumentUseCaseUtil(contactInformationRepository, taxInformationRepository,
                taxCountryRepository, foreignInformationRepository, foreignInformationCurrencyRepository,
                constructResponsesUseCase);
    }
}