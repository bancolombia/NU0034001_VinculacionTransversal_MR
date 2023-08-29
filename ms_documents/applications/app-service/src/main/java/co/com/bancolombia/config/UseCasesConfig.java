package co.com.bancolombia.config;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.acquisition.AcquisitionUseCaseImpl;
import co.com.bancolombia.api.customerdocumentpersistence.CustomerDocumentPersistenceLogController;
import co.com.bancolombia.api.genericstep.GenericStep;
import co.com.bancolombia.api.uploaddocument.UploadDocumentControllerEco;
import co.com.bancolombia.api.uploaddocument.UploadDocumentControllerFields;
import co.com.bancolombia.api.uploaddocument.UploadDocumentControllerUtil;
import co.com.bancolombia.asyncdigitalization.AsyncDigitalizationUseCase;
import co.com.bancolombia.asyncdigitalization.AsyncDigitalizationUseCaseImpl;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.digitalizationprocesseddocuments.DigitalizationProcessedDocumentsUseCase;
import co.com.bancolombia.digitalizationprocesseddocuments.DigitalizationProcessedDocumentsUseCaseImpl;
import co.com.bancolombia.documentretries.DocumentRetriesUseCase;
import co.com.bancolombia.documentretries.DocumentRetriesUseCaseImpl;
import co.com.bancolombia.generateexposedocuments.GenExpDocsUseCase;
import co.com.bancolombia.generateexposedocuments.GenExpDocsUseCaseImpl;
import co.com.bancolombia.generatepdf.GenPdfA1B;
import co.com.bancolombia.generatepdf.GenPdfA1BImpl;
import co.com.bancolombia.generatepdf.GeneratePdfConvertJson;
import co.com.bancolombia.generatepdf.GeneratePdfConvertJsonImpl;
import co.com.bancolombia.generatepdf.GeneratePdfDocumentUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfDocumentUseCaseImpl;
import co.com.bancolombia.generatepdf.GeneratePdfProcessUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfProcessUseCaseImpl;
import co.com.bancolombia.generatepdf.GeneratePdfTaxUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfTaxUseCaseImpl;
import co.com.bancolombia.generatepdf.GeneratePdfUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfUseCaseImpl;
import co.com.bancolombia.generatepdf.GeneratePdfUtilAmazon;
import co.com.bancolombia.generatepdf.GeneratePdfUtilAmazonImpl;
import co.com.bancolombia.generatepdf.GeneratePdfUtilDocsUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfUtilDocsUseCaseImpl;
import co.com.bancolombia.generatepdf.GeneratePdfUtilOneUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfUtilOneUseCaseImpl;
import co.com.bancolombia.generatepdf.GeneratePdfUtilTwoUseCase;
import co.com.bancolombia.generatepdf.GeneratePdfUtilTwoUseCaseImpl;
import co.com.bancolombia.model.asyncdigitalization.gateways.AsyncDigitalizationRepository;
import co.com.bancolombia.model.digitalizationprocesseddocuments.gateways.DigitalizationProcessedDocumentsRepository;
import co.com.bancolombia.model.documentretries.gateways.DocumentRetriesRepository;
import co.com.bancolombia.model.generatepdf.gateways.GeneratePdfRepository;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.pdfobject.gateway.PdfObjectRepository;
import co.com.bancolombia.model.persistencedocument.gateways.DocumentPersistenceRestRepository;
import co.com.bancolombia.model.persistencedocument.gateways.PersistenceDocumentRepository;
import co.com.bancolombia.model.persistencedocument.gateways.ServicePersistenceRestRepository;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRepository;
import co.com.bancolombia.model.signdocument.gateways.SignDocumentRestRepository;
import co.com.bancolombia.model.signdocument.gateways.TxtConstructionRepository;
import co.com.bancolombia.model.sqs.gateways.SqsMessageRepository;
import co.com.bancolombia.model.uploaddocument.gateways.DigitalizationIdentityRepository;
import co.com.bancolombia.model.uploaddocument.gateways.DigitalizationRutRepository;
import co.com.bancolombia.model.uploaddocument.gateways.ProcessDocumentRestRepository;
import co.com.bancolombia.model.uploadedfile.gateways.DataFileRepository;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.parameters.ParametersUseCaseImpl;
import co.com.bancolombia.persistencedocument.ImageConverter;
import co.com.bancolombia.persistencedocument.PdfMerger;
import co.com.bancolombia.persistencedocument.PersistenceDocumentUseCase;
import co.com.bancolombia.persistencedocument.PersistenceDocumentUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceDocumentValidateUseCase;
import co.com.bancolombia.persistencedocument.PersistenceDocumentValidateUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceDocumentValidationUseCase;
import co.com.bancolombia.persistencedocument.PersistenceDocumentValidationUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceProcessUseCase;
import co.com.bancolombia.persistencedocument.PersistenceProcessUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceQueueUseCase;
import co.com.bancolombia.persistencedocument.PersistenceQueueUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceValidateDocUseCase;
import co.com.bancolombia.persistencedocument.PersistenceValidateDocUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceValidateTypeDocument;
import co.com.bancolombia.persistencedocument.PersistenceValidateTypeDocumentImpl;
import co.com.bancolombia.persistencedocument.PersistenceValidationsRetriesUseCase;
import co.com.bancolombia.persistencedocument.PersistenceValidationsRetriesUseCaseImpl;
import co.com.bancolombia.persistencedocument.PersistenceValidationsUseCase;
import co.com.bancolombia.persistencedocument.PersistenceValidationsUseCaseImpl;
import co.com.bancolombia.persistencedocument.RetriesPersistenceDocumentUseCase;
import co.com.bancolombia.persistencedocument.RetriesPersistenceDocumentUseCaseImpl;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.NaturalPersonUseCaseImpl;
import co.com.bancolombia.rabbit.ReputationIdentityUseCase;
import co.com.bancolombia.rabbit.ReputationIdentityUseCaseImpl;
import co.com.bancolombia.rabbit.TriggerExceptionUseCase;
import co.com.bancolombia.rabbit.TriggerExceptionUseCaseImpl;
import co.com.bancolombia.rabbit.VinculationUpdateTwoUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateTwoUseCaseImpl;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCaseImpl;
import co.com.bancolombia.signdocument.SignDocumentRequestUseCase;
import co.com.bancolombia.signdocument.SignDocumentRequestUseCaseImpl;
import co.com.bancolombia.signdocument.SignDocumentResponseUseCase;
import co.com.bancolombia.signdocument.SignDocumentResponseUseCaseImpl;
import co.com.bancolombia.signdocument.SignDocumentUseCase;
import co.com.bancolombia.signdocument.SignDocumentUseCaseImpl;
import co.com.bancolombia.signdocument.SignDocumentValidateUseCase;
import co.com.bancolombia.signdocument.SignDocumentValidateUseCaseImpl;
import co.com.bancolombia.signdocument.txt.SDTxtAttachUseCase;
import co.com.bancolombia.signdocument.txt.SDTxtAttachUseCaseImpl;
import co.com.bancolombia.signdocument.txt.SDTxtFinalUseCase;
import co.com.bancolombia.signdocument.txt.SDTxtFinalUseCaseImpl;
import co.com.bancolombia.signdocument.txt.SDTxtInitialUseCase;
import co.com.bancolombia.signdocument.txt.SDTxtInitialUseCaseImpl;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtClauseUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtEightUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtFiveUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtFourUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtNineUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtSevenUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtSixUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTenUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtThreeUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTwelveUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtTwoUseCase;
import co.com.bancolombia.signdocument.txt.iterations.SDTxtUtilUseCase;
import co.com.bancolombia.sqs.SqsMessObjUploadDocUtil;
import co.com.bancolombia.sqs.SqsMessObjUploadDocUtilImpl;
import co.com.bancolombia.sqs.SqsMessageJsonUseCase;
import co.com.bancolombia.sqs.SqsMessageJsonUseCaseImpl;
import co.com.bancolombia.sqs.SqsMessageParameterUseCase;
import co.com.bancolombia.sqs.SqsMessageParameterUseCaseImpl;
import co.com.bancolombia.sqs.SqsMessageSaveUseCase;
import co.com.bancolombia.sqs.SqsMessageSaveUseCaseImpl;
import co.com.bancolombia.sqs.SqsMessageUplDocAcqUseCase;
import co.com.bancolombia.sqs.SqsMessageUplDocAcqUseCaseImpl;
import co.com.bancolombia.sqs.SqsMessageUplDocUseCase;
import co.com.bancolombia.sqs.SqsMessageUplDocUseCaseImpl;
import co.com.bancolombia.sqs.SqsMessageUplDocUtilUseCase;
import co.com.bancolombia.sqs.SqsMessageUplDocUtilUseCaseImpl;
import co.com.bancolombia.sqs.SqsMessageUseCase;
import co.com.bancolombia.sqs.SqsMessageUseCaseImpl;
import co.com.bancolombia.uploaddocument.ProcessDocumentSaveUseCase;
import co.com.bancolombia.uploaddocument.ProcessDocumentSaveUseCaseImpl;
import co.com.bancolombia.uploaddocument.ProcessDocumentUseCase;
import co.com.bancolombia.uploaddocument.ProcessDocumentUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentCcRulesUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentCcRulesUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentExcepUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentExcepUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessedDocumentsUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentProcessedDocumentsUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentRutModifyCiiuUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutModifyCiiuUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentRutRulesUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutRulesUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentRutSaveUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutSaveUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentRutUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentRutUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentSaveUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentSaveUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentSyncUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentSyncUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentUseCaseImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateErrors;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateErrorsImpl;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateUseCase;
import co.com.bancolombia.uploaddocument.UploadDocumentValidateUseCaseImpl;
import co.com.bancolombia.util.TransformFields;
import co.com.bancolombia.validatedataextraction.ValidateDataExtractionLogUseCase;
import co.com.bancolombia.validatedataextraction.ValidateDataExtractionLogUseCaseImpl;
import co.com.bancolombia.validatedataextraction.ValidateDataExtractionUseCase;
import co.com.bancolombia.validatedataextraction.ValidateDataExtractionUseCaseImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityRulePhonetics;
import co.com.bancolombia.validateidentity.ValidateIdentityRulePhoneticsImpl;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUtilUseCase;
import co.com.bancolombia.validateidentity.ValidateIdentityRuleUtilUseCaseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class UseCasesConfig {

    @Bean
    public CoreFunctionDate createModuleCoreFunctionDate() {
        return new CoreFunctionDate();
    }

    @Bean
    public TriggerExceptionUseCase createModuleTriggerException() {
        return new TriggerExceptionUseCaseImpl();
    }

    @Bean
    public NaturalPersonUseCase createModuleNaturalPerson(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase) {
        return new NaturalPersonUseCaseImpl(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public VinculationUpdateUseCase createModuleVinculationUpdate(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase) {
        return new VinculationUpdateUseCaseImpl(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public VinculationUpdateTwoUseCase createModuleVinculationUpdateTwo(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase) {
        return new VinculationUpdateTwoUseCaseImpl(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public AsyncDigitalizationUseCase createModuleAsyncDigitalization(AsyncDigitalizationRepository repository) {
        return new AsyncDigitalizationUseCaseImpl(repository);
    }

    @Bean
    public DigitalizationProcessedDocumentsUseCase createModuleDigitalizationProcessedDocuments(
            DigitalizationProcessedDocumentsRepository repository) {
        return new DigitalizationProcessedDocumentsUseCaseImpl(repository);
    }

    @Bean
    public ParametersUseCase createModuleParameters(ParametersRepository repository) {
        return new ParametersUseCaseImpl(repository);
    }

    @Bean
    public SqsMessageParameterUseCase createModuleSqsMessageParameter(ParametersUseCase parametersUseCase) {
        return new SqsMessageParameterUseCaseImpl(parametersUseCase);
    }

    @Bean
    public SqsMessageSaveUseCase createModuleSqsMessageSave(SqsMessageUseCase sqsMessageUseCase,
                                                            AsyncDigitalizationUseCase asyncDigitalizationUseCase,
                                                            SqsMessObjUploadDocUtil sqsMessObjUploadDocUtil) {
        return new SqsMessageSaveUseCaseImpl(sqsMessageUseCase, asyncDigitalizationUseCase, sqsMessObjUploadDocUtil);
    }

    @Bean
    public SqsMessageUplDocAcqUseCase createModuleSqsMessageUplDocAcq() {
        return new SqsMessageUplDocAcqUseCaseImpl();
    }

    @Bean
    public SqsMessageUplDocUseCase createModuleSqsMessageUplDoc(
            UploadDocumentValidateUseCase uploadDocumentValidateUseCase, UploadDocumentUseCase uploadDocumentUseCase,
            UploadDocumentRutUseCase uploadDocumentRutUseCase, SqsMessageUplDocUtilUseCase sqsMessageUplDocUseCaseUtil,
            SqsMessageSaveUseCase sqsMessageSaveUseCase,
            UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase,
            AcquisitionUseCase acquisitionUseCase) {
        return new SqsMessageUplDocUseCaseImpl(uploadDocumentValidateUseCase, uploadDocumentUseCase,
                uploadDocumentRutUseCase, sqsMessageUplDocUseCaseUtil, sqsMessageSaveUseCase,
                uploadDocumentProcessedDocumentsUseCase, acquisitionUseCase);
    }

    @Bean
    public SqsMessageUplDocUtilUseCase createModuleSqsMessageUplDocUtil(
            SqsMessageUplDocAcqUseCase sqsMessageUplDocUseCaseAcq) {
        return new SqsMessageUplDocUtilUseCaseImpl(sqsMessageUplDocUseCaseAcq);
    }

    @Bean
    public SqsMessageUseCase createModuleSqsMessage(
            SqsMessageRepository sqsMessageRepository, CoreFunctionDate coreFunctionDate,
            SqsMessageParameterUseCase sqsMessageParameterUsesCase, SqsAsyncClient sqsAsyncClient,
            SqsMessageJsonUseCase sqsMessageJsonUseCase) {
        return new SqsMessageUseCaseImpl(sqsMessageRepository, coreFunctionDate, sqsMessageParameterUsesCase,
                sqsAsyncClient, sqsMessageJsonUseCase);
    }

    @Bean
    public SqsMessObjUploadDocUtil createModuleSqsMessObjUploadDocUtil(
            SqsMessageUplDocUtilUseCase sqsMessageUplDocUseCaseUtil) {
        return new SqsMessObjUploadDocUtilImpl(sqsMessageUplDocUseCaseUtil);
    }

    @Bean
    public UploadDocumentExcepUseCase createModuleUploadDocumentExcep(
            VinculationUpdateUseCase vinculationUpdateUseCase, @Lazy SqsMessageUplDocUseCase sqsMessageUplDocUsecase,
            UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase,
            DocumentRetriesUseCase documentRetriesUseCase) {
        return new UploadDocumentExcepUseCaseImpl(vinculationUpdateUseCase, sqsMessageUplDocUsecase,
                uploadDocumentProcessedDocumentsUseCase, documentRetriesUseCase);
    }

    @Bean
    public UploadDocumentProcessedDocumentsUseCase createModuleUploadDocumentProcessedDocuments(
            CoreFunctionDate coreFunctionDate,
            DigitalizationProcessedDocumentsUseCase digitalizationProcessedDocumentsUseCase,
            UploadDocumentSyncUseCase uploadDocumentSyncUseCase, DataFileRepository dataFileRepository) {
        return new UploadDocumentProcessedDocumentsUseCaseImpl(coreFunctionDate,
                digitalizationProcessedDocumentsUseCase, uploadDocumentSyncUseCase, dataFileRepository);
    }

    @Bean
    public UploadDocumentRutModifyCiiuUseCase createModuleUploadDocumentRutModifyCiiu(
            VinculationUpdateUseCase vinculationUpdateUseCase, NaturalPersonUseCase naturalPersonUseCase,
            UploadDocumentValidateErrors uploadDocumentValidateErrors) {
        return new UploadDocumentRutModifyCiiuUseCaseImpl(
                vinculationUpdateUseCase, naturalPersonUseCase, uploadDocumentValidateErrors);
    }

    @Bean
    public UploadDocumentRutSaveUseCase createModuleUploadDocumentRutSave(
            NaturalPersonUseCase naturalPersonUseCase, ValidateIdentityRuleUtilUseCase validateIdentityRuleUtilUseCase,
            ParametersUseCase parametersUseCase, UploadDocumentRutModifyCiiuUseCase uploadDocumentRutModifyCiiuUseCase,
            CoreFunctionDate coreFunctionDate, UploadDocumentValidateErrors uploadDocumentValidateErrors) {
        return new UploadDocumentRutSaveUseCaseImpl(naturalPersonUseCase, validateIdentityRuleUtilUseCase,
                parametersUseCase, uploadDocumentRutModifyCiiuUseCase, coreFunctionDate, uploadDocumentValidateErrors);
    }

    @Bean
    public UploadDocumentRutUseCase createModuleUploadDocumentRut(
            VinculationUpdateUseCase vinculationUpdateUseCase, UploadDocumentSaveUseCase uplDocSaveUseCase,
            UploadDocumentUseCase uploadDocumentUseCase, UploadDocumentRutSaveUseCase uploadDocumentRutSaveUseCase,
            UploadDocumentExcepUseCase uploadDocumentExcepUseCase, UploadDocumentSyncUseCase uploadDocumentSyncUseCase,
            UploadDocumentProcessUseCase uploadDocumentProcessUseCase) {
        return new UploadDocumentRutUseCaseImpl(vinculationUpdateUseCase, uplDocSaveUseCase, uploadDocumentUseCase,
                uploadDocumentRutSaveUseCase, uploadDocumentExcepUseCase, uploadDocumentSyncUseCase,
                uploadDocumentProcessUseCase);
    }

    @Bean
    public UploadDocumentSaveUseCase createModuleUploadDocumentSave(
            CoreFunctionDate coreFunctionDate, NaturalPersonUseCase naturalPersonUseCase,
            VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new UploadDocumentSaveUseCaseImpl(
                coreFunctionDate, naturalPersonUseCase, vinculationUpdateUseCase);
    }

    @Bean
    public UploadDocumentSyncUseCase createModuleUploadDocumentSync(SqsMessageUseCase sqsMessageUseCase) {
        return new UploadDocumentSyncUseCaseImpl(sqsMessageUseCase);
    }

    @Bean
    public UploadDocumentUseCase createModuleUploadDocument(
            VinculationUpdateUseCase vinculationUpdateUseCase, UploadDocumentSaveUseCase uploadDocumentSaveUseCase,
            UploadDocumentExcepUseCase uplDocExcepUseCase, UploadDocumentSyncUseCase uploadDocumentSyncUseCase,
            UploadDocumentValidateErrors uploadDocumentValidateErrors,
            UploadDocumentProcessUseCase uploadDocumentProcessUseCase) {
        return new UploadDocumentUseCaseImpl(vinculationUpdateUseCase, uploadDocumentSaveUseCase, uplDocExcepUseCase,
                uploadDocumentSyncUseCase, uploadDocumentValidateErrors, uploadDocumentProcessUseCase);
    }

    @Bean
    public UploadDocumentValidateErrors createModuleUploadDocumentValidateErrors(
            VinculationUpdateUseCase vinculationUpdateUseCase, ParametersUseCase parametersUseCase,
            UploadDocumentExcepUseCase uplDocExcepUseCase, DocumentRetriesUseCase documentRetriesUseCase) {
        return new UploadDocumentValidateErrorsImpl(
                vinculationUpdateUseCase, parametersUseCase, uplDocExcepUseCase, documentRetriesUseCase);
    }

    @Bean
    public UploadDocumentValidateUseCase createModuleUploadDocumentValidate(
            VinculationUpdateUseCase vinculationUpdateUseCase, NaturalPersonUseCase naturalPersonUseCase,
            DataFileRepository dataFileRepository, UploadDocumentExcepUseCase uploadDocumentExcepUseCase) {
        return new UploadDocumentValidateUseCaseImpl(
                vinculationUpdateUseCase, naturalPersonUseCase, dataFileRepository, uploadDocumentExcepUseCase);
    }

    @Bean
    public ValidateIdentityRulePhonetics createModuleValidateIdentityRulePhonetics() {
        return new ValidateIdentityRulePhoneticsImpl();
    }

    @Bean
    public ValidateIdentityRuleUtilUseCase createModuleValidateIdentityRuleUtil(
            ValidateIdentityRulePhonetics validateIdentityRulePhonetics) {
        return new ValidateIdentityRuleUtilUseCaseImpl(validateIdentityRulePhonetics);
    }

    @Bean
    public UploadDocumentControllerUtil createModuleUploadDocumentControllerUtil(
            UploadDocumentUseCase uploadDocumentUseCase, UploadDocumentRutUseCase uploadDocumentRutUseCase,
            UploadDocumentControllerEco uploadDocumentControllerEco, VinculationUpdateUseCase vinculationUpdateUseCase,
            DocumentRetriesUseCase documentRetriesUseCase, UploadDocumentControllerFields uploadDocControllerFields) {
        return new UploadDocumentControllerUtil(
                uploadDocumentUseCase, uploadDocumentRutUseCase, uploadDocumentControllerEco,
                vinculationUpdateUseCase, documentRetriesUseCase, uploadDocControllerFields);
    }

    @Bean
    public UploadDocumentControllerEco createModuleUploadDocumentControllerEco(
            NaturalPersonUseCase naturalPersonUseCase,
            UploadDocumentProcessedDocumentsUseCase uploadDocumentProcessedDocumentsUseCase) {
        return new UploadDocumentControllerEco(naturalPersonUseCase, uploadDocumentProcessedDocumentsUseCase);
    }

    @Bean
    public UploadDocumentControllerFields createModuleUploadDocumentControllerFields() {
        return new UploadDocumentControllerFields();
    }

    @Bean
    public ProcessDocumentSaveUseCase createModuleProcessDocumentSave(
            DigitalizationIdentityRepository digitalizationIdentityRepository,
            DigitalizationRutRepository digitalizationRutRepository, CoreFunctionDate coreFunctionDate) {
        return new ProcessDocumentSaveUseCaseImpl(
                digitalizationIdentityRepository, digitalizationRutRepository, coreFunctionDate);
    }

    @Bean
    public ProcessDocumentUseCase createModuleProcessDocument(
            ProcessDocumentRestRepository processDocumentRestRepository, CoreFunctionDate coreFunctionDate,
            ProcessDocumentSaveUseCase processDocumentSaveUseCase) {
        return new ProcessDocumentUseCaseImpl(
                processDocumentRestRepository, coreFunctionDate, processDocumentSaveUseCase);
    }

    @Bean
    public UploadDocumentCcRulesUseCase createModuleUploadDocumentCcRules(
            ParametersUseCase parametersUseCase, CoreFunctionDate coreFunctionDate) {
        return new UploadDocumentCcRulesUseCaseImpl(parametersUseCase, coreFunctionDate);
    }

    @Bean
    public UploadDocumentRutRulesUseCase createModuleUploadDocumentRutRules(ParametersUseCase parametersUseCase) {
        return new UploadDocumentRutRulesUseCaseImpl(parametersUseCase);
    }

    @Bean
    public UploadDocumentProcessUseCase createModuleUploadDocumentProcess(
            ProcessDocumentUseCase processDocumentUseCase, UploadDocumentCcRulesUseCase uploadDocumentCcRulesUseCase,
            UploadDocumentRutRulesUseCase uploadDocumentRutRulesUseCase) {
        return new UploadDocumentProcessUseCaseImpl(
                processDocumentUseCase, uploadDocumentCcRulesUseCase, uploadDocumentRutRulesUseCase);
    }

    @Bean
    public DocumentRetriesUseCase createModuleDocumentRetries(
            DocumentRetriesRepository repository, CoreFunctionDate coreFunctionDate) {
        return new DocumentRetriesUseCaseImpl(repository, coreFunctionDate);
    }

    @Bean
    public SqsMessageJsonUseCase createModuleSqsMessageJson() {
        return new SqsMessageJsonUseCaseImpl();
    }

    @Bean
    public AcquisitionUseCase createModuleAcquisition(DocumentRetriesUseCase documentRetriesUseCase) {
        return new AcquisitionUseCaseImpl(documentRetriesUseCase);
    }

    @Bean
    public ValidateDataExtractionUseCase createModuleValidateDataExtraction(
            AsyncDigitalizationUseCase asyncDigitalizationUseCase, SqsMessageUseCase sqsMessageUseCase,
            VinculationUpdateUseCase vinculationUpdateUseCase, UploadDocumentExcepUseCase uploadDocumentExcepUseCase,
            ValidateDataExtractionLogUseCase validateDataExtractionLogUseCase) {
        return new ValidateDataExtractionUseCaseImpl(asyncDigitalizationUseCase, sqsMessageUseCase,
                vinculationUpdateUseCase, uploadDocumentExcepUseCase, validateDataExtractionLogUseCase);
    }

    @Bean
    public ValidateDataExtractionLogUseCase createModuleValidateDataExtractionLog() {
        return new ValidateDataExtractionLogUseCaseImpl();
    }

    @Bean
    public Exceptions createModuleExceptions() {
        return new Exceptions();
    }

    @Bean
    public SignDocumentUseCase createModuleSignDocument(
            SignDocumentValidateUseCase signDocumentUseCase, SignDocumentRequestUseCase createRequest,
            SignDocumentRepository repository, CoreFunctionDate coreFunctionDate,
            VinculationUpdateUseCase vinculationUpdateUseCase, SignDocumentRestRepository signDocumentRestRepository) {
        return new SignDocumentUseCaseImpl(signDocumentUseCase, createRequest, repository, coreFunctionDate,
                vinculationUpdateUseCase, signDocumentRestRepository);
    }

    @Bean
    public SignDocumentRequestUseCase createModuleSignDocumentRequest(
            DataFileRepository dataFileRepository, NaturalPersonUseCase naturalPersonUseCase,
            SDTxtAttachUseCase sdTxtAttachUseCase, GeneratePdfUseCase generatePdfUseCase) {
        return new SignDocumentRequestUseCaseImpl(dataFileRepository, naturalPersonUseCase, sdTxtAttachUseCase,
                generatePdfUseCase);
    }

    @Bean
    public SignDocumentResponseUseCase createModuleSignDocumentResponse(ObjectMapper objectMapper) {
        return new SignDocumentResponseUseCaseImpl(objectMapper);
    }

    @Bean
    public SignDocumentValidateUseCase createModuleSignDocumentValidate(
            ParametersUseCase parametersUseCase, Exceptions exceptions,
            VinculationUpdateUseCase vinculationUpdateUseCase,
            VinculationUpdateTwoUseCase vinculationUpdateTwoUseCase) {
        return new SignDocumentValidateUseCaseImpl(parametersUseCase, exceptions, vinculationUpdateUseCase,
                vinculationUpdateTwoUseCase);
    }

    @Bean
    public SDTxtAttachUseCase createModuleSDTxtAttach(SDTxtInitialUseCase sdTxtInitialUseCase) {
        return new SDTxtAttachUseCaseImpl(sdTxtInitialUseCase);
    }

    @Bean
    public SDTxtInitialUseCase createModuleSignDocumentTxtInitialUseCase(
            SDTxtFinalUseCase sdTxtFinalUseCase, SDTxtClauseUseCase sdTxtClauseUseCase, SDTxtTwoUseCase sdTxtTwoUseCase,
            SDTxtThreeUseCase sdTxtThreeUseCase, SDTxtFourUseCase sdTxtFourUseCase, SDTxtFiveUseCase sdTxtFiveUseCase,
            SDTxtSixUseCase sdTxtSixUseCase, ReputationIdentityUseCase reputationIdentityUseCase) {
        return new SDTxtInitialUseCaseImpl(sdTxtFinalUseCase, sdTxtClauseUseCase, sdTxtTwoUseCase, sdTxtThreeUseCase,
                sdTxtFourUseCase, sdTxtFiveUseCase, sdTxtSixUseCase, reputationIdentityUseCase);
    }

    @Bean
    public SDTxtFinalUseCase createModuleCreateModuleSDTxtFinal(
            SDTxtClauseUseCase sdTxtClauseUseCase, SDTxtSevenUseCase sdTxtSevenUseCase,
            SDTxtEightUseCase sdTxtEightUseCase, SDTxtNineUseCase sdTxtNineUseCase, SDTxtTenUseCase sdTxtTenUseCase,
            SDTxtTwelveUseCase sdTxtTwelveUseCase) {
        return new SDTxtFinalUseCaseImpl(sdTxtClauseUseCase, sdTxtSevenUseCase, sdTxtEightUseCase, sdTxtNineUseCase,
                sdTxtTenUseCase, sdTxtTwelveUseCase);
    }

    @Bean
    public SDTxtUtilUseCase createModuleSDTxtUtil(TxtConstructionRepository txtConstructionRepository) {
        return new SDTxtUtilUseCase(txtConstructionRepository);
    }

    @Bean
    public SDTxtClauseUseCase createModuleSDTxtClauseUseCase(
            SDTxtUtilUseCase genericUseCase, VinculationUpdateTwoUseCase vinculationUpdateUseCase) {
        return new SDTxtClauseUseCase(genericUseCase, vinculationUpdateUseCase);
    }

    @Bean
    public SDTxtTwoUseCase createModuleSDtwoIterationUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtTwoUseCase(genericUseCase);
    }

    @Bean
    public SDTxtThreeUseCase createModuleSDTxtThreeUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtThreeUseCase(genericUseCase);
    }

    @Bean
    public SDTxtFourUseCase createModuleSDTxtFourUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtFourUseCase(genericUseCase);
    }

    @Bean
    public SDTxtFiveUseCase createModuleSDTxtFiveUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtFiveUseCase(genericUseCase);
    }

    @Bean
    public SDTxtSixUseCase createModuleSDTxtSixUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtSixUseCase(genericUseCase);
    }

    @Bean
    public SDTxtSevenUseCase createModuleSDTxtSevenUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtSevenUseCase(genericUseCase);
    }

    @Bean
    public SDTxtEightUseCase createModuleSDTxtEightUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtEightUseCase(genericUseCase);
    }

    @Bean
    public SDTxtNineUseCase createModuleSDTxtNineUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtNineUseCase(genericUseCase);
    }

    @Bean
    public SDTxtTenUseCase sdTxtTenUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtTenUseCase(genericUseCase);
    }

    @Bean
    public SDTxtTwelveUseCase sdTxtTwelveUseCase(SDTxtUtilUseCase genericUseCase) {
        return new SDTxtTwelveUseCase(genericUseCase);
    }

    @Bean
    public ReputationIdentityUseCase createModuleReputationIdentity(
            DirectAsyncGateway directAsyncGateway, TriggerExceptionUseCase triggerExceptionUseCase) {
        return new ReputationIdentityUseCaseImpl(directAsyncGateway, triggerExceptionUseCase);
    }

    @Bean
    public ImageConverter createImageConverter() {
        return new ImageConverter();
    }

    @Bean
    public PdfMerger createPdfMerger(DataFileRepository dataFileRepository) {
        return new PdfMerger(dataFileRepository);
    }

    @Bean
    public PersistenceDocumentUseCase createPersistenceDocumentUseCase(
            DigitalizationProcessedDocumentsUseCase digitalizationProcessedDocumentsUseCase,
            NaturalPersonUseCase naturalPersonUseCase,
            PersistenceDocumentValidationUseCase persistenceDocumentValidationUseCase,
            PersistenceProcessUseCase persistenceProcessUseCase,
            RetriesPersistenceDocumentUseCase retriesPersistenceDocumentUseCase) {
        return new PersistenceDocumentUseCaseImpl(digitalizationProcessedDocumentsUseCase, naturalPersonUseCase,
                persistenceDocumentValidationUseCase, persistenceProcessUseCase, retriesPersistenceDocumentUseCase);
    }

    @Bean
    public PersistenceDocumentValidateUseCase createPersistenceDocumentValidateUseCase(
            DocumentPersistenceRestRepository documentPersistenceRestRepository,
            DataFileRepository dataFileRepositoryAdapter, ParametersUseCase parametersUseCase,
            PersistenceValidateTypeDocument persistenceValidateTypeDocument) {
        return new PersistenceDocumentValidateUseCaseImpl(documentPersistenceRestRepository, dataFileRepositoryAdapter,
                parametersUseCase, persistenceValidateTypeDocument);
    }

    @Bean
    public PersistenceDocumentValidationUseCase createPersistenceDocumentValidationUseCase(
            PersistenceDocumentValidateUseCase persistenceDocumentValidateUseCase,
            PersistenceQueueUseCase persistenceQueueUseCase, CoreFunctionDate coreFunctionDate,
            PersistenceDocumentRepository persistenceDocumentRepository) {
        return new PersistenceDocumentValidationUseCaseImpl(persistenceDocumentValidateUseCase, persistenceQueueUseCase,
                coreFunctionDate, persistenceDocumentRepository);
    }

    @Bean
    public PersistenceProcessUseCase createPersistenceProcessUseCase(PersistenceValidationsUseCase pValidationsUC,
                                                                     PersistenceValidateDocUseCase pValDocUC) {
        return new PersistenceProcessUseCaseImpl(pValidationsUC, pValDocUC);
    }

    @Bean
    public PersistenceQueueUseCase createPersistenceQueueUseCase(
            SqsAsyncClient sqsAsyncClient, CoreFunctionDate coreFunctionDate,
            @Lazy PersistenceDocumentValidationUseCase persistenceDocumentUseCase,
            ServicePersistenceRestRepository microServiceRestRepository) {
        return new PersistenceQueueUseCaseImpl(sqsAsyncClient, coreFunctionDate, persistenceDocumentUseCase,
                microServiceRestRepository);
    }

    @Bean
    public PersistenceValidateTypeDocument createPersistenceValidateTypeDocument(
            PdfMerger pdfMerger, ImageConverter imageConverter, DataFileRepository dataFileRepositoryAdapter) {
        return new PersistenceValidateTypeDocumentImpl(pdfMerger, imageConverter, dataFileRepositoryAdapter);
    }

    @Bean
    public PersistenceValidationsUseCase createPersistenceValidationsUseCaseImpl(
            VinculationUpdateUseCase vinculationUpdateUseCase, DataFileRepository dataFileRepository) {
        return new PersistenceValidationsUseCaseImpl(vinculationUpdateUseCase, dataFileRepository);
    }

    @Bean
    public PersistenceValidateDocUseCase createPersistenceValidateDocUseCase(
            VinculationUpdateUseCase vinculationUpdateUseCase, Exceptions exceptions,
            PersistenceValidationsUseCase persistenceValidationsUseCase) {
        return new PersistenceValidateDocUseCaseImpl(vinculationUpdateUseCase, exceptions,
                persistenceValidationsUseCase);
    }

    @Bean
    public PersistenceValidationsRetriesUseCase createPersistenceValidationsRetriesUseCase(
            VinculationUpdateUseCase vinculationUpdateUseCase, DataFileRepository dataFileRepository) {
        return new PersistenceValidationsRetriesUseCaseImpl(vinculationUpdateUseCase, dataFileRepository);
    }

    @Bean
    public RetriesPersistenceDocumentUseCase createRetriesPersistenceDocumentUseCase(
            PersistenceValidationsRetriesUseCase pVRetriesUC) {
        return new RetriesPersistenceDocumentUseCaseImpl(pVRetriesUC);
    }

    @Bean
    public CustomerDocumentPersistenceLogController createCustomerDocumentPersistenceLogController(
            GenericStep genericStep) {
        return new CustomerDocumentPersistenceLogController(genericStep);
    }

    @Bean
    public TransformFields createModuleTransformFields(CoreFunctionDate coreFunctionDate) {
        return new TransformFields(coreFunctionDate);
    }

    @Bean
    public GeneratePdfConvertJson createModuleGeneratePdfConvertJson(TransformFields transformFields) {
        return new GeneratePdfConvertJsonImpl(transformFields);
    }

    @Bean
    public GeneratePdfDocumentUseCase createModuleGeneratePdfDocument(
            DataFileRepository dataFileRepository, @Lazy GeneratePdfUtilAmazon generatePdfUtilAmazon,
            GeneratePdfUtilDocsUseCase generatePdfUtilDocsUseCase) {
        return new GeneratePdfDocumentUseCaseImpl(
                dataFileRepository, generatePdfUtilAmazon, generatePdfUtilDocsUseCase);
    }

    @Bean
    public GeneratePdfProcessUseCase createModuleGeneratePdfProcess(
            GeneratePdfDocumentUseCase generatePdfDocumentUseCase) {
        return new GeneratePdfProcessUseCaseImpl(generatePdfDocumentUseCase);
    }

    @Bean
    public GeneratePdfTaxUseCase createModuleGeneratePdfTax(GeneratePdfUtilOneUseCase generatePdfUtilOneUseCase) {
        return new GeneratePdfTaxUseCaseImpl(generatePdfUtilOneUseCase);
    }

    @Bean
    public GeneratePdfUseCase createModuleGeneratePdf(
            GeneratePdfProcessUseCase generatePdfProcessUseCase, GeneratePdfUtilTwoUseCase generatePdfUtilTwoUseCase,
            GeneratePdfRepository generatePdfRepository, CoreFunctionDate coreFunctionDate) {
        return new GeneratePdfUseCaseImpl(
                generatePdfProcessUseCase, generatePdfUtilTwoUseCase, generatePdfRepository, coreFunctionDate);
    }

    @Bean
    public GeneratePdfUtilAmazon createModuleGeneratePdfUtilAmazon(
            DataFileRepository dataFileRepository, GeneratePdfDocumentUseCase generatePdfDocumentUseCase,
            ParametersRepository parametersRepository, GenPdfA1B genPdfA1B) {
        return new GeneratePdfUtilAmazonImpl(
                dataFileRepository, generatePdfDocumentUseCase, parametersRepository, genPdfA1B);
    }

    @Bean
    public GeneratePdfUtilDocsUseCase createModuleGeneratePdfUtilDocs(
            ParametersRepository parametersRepository, PdfObjectRepository pdfObjectRepository) {
        return new GeneratePdfUtilDocsUseCaseImpl(parametersRepository, pdfObjectRepository);
    }

    @Bean
    public GeneratePdfUtilOneUseCase createModuleGeneratePdfUtilOne(VinculationUpdateUseCase vinculationUpdateUseCase) {
        return new GeneratePdfUtilOneUseCaseImpl(vinculationUpdateUseCase);
    }

    @Bean
    public GeneratePdfUtilTwoUseCase createModuleGeneratePdfUtilTwo(
            NaturalPersonUseCase naturalPersonUseCase, GeneratePdfUtilOneUseCase generatePdfUtilOneUseCase,
            GeneratePdfTaxUseCase generatePdfTaxUseCase, GeneratePdfConvertJson generatePdfConvertJson) {
        return new GeneratePdfUtilTwoUseCaseImpl(
                naturalPersonUseCase, generatePdfUtilOneUseCase, generatePdfTaxUseCase, generatePdfConvertJson);
    }

    @Bean
    public GenPdfA1B createModuleGenPdfA1B() {
        return new GenPdfA1BImpl();
    }

    @Bean
    public GenExpDocsUseCase createModuleGenExpDocs(
            DataFileRepository dataFileRepository, VinculationUpdateUseCase vinculationUpdateUseCase,
            @Lazy GeneratePdfUseCase generatePdfUseCase, Exceptions exceptions, CoreFunctionDate coreFunctionDate) {
        return new GenExpDocsUseCaseImpl(
                dataFileRepository, vinculationUpdateUseCase, generatePdfUseCase, exceptions, coreFunctionDate);
    }
}
