package co.com.bancolombia.config;

import co.com.bancolombia.acquisition.AcquisitionOpeValidateUseCase;
import co.com.bancolombia.acquisition.AcquisitionOpeValidateUseCaseImpl;
import co.com.bancolombia.acquisition.AcquisitionOperationUseCase;
import co.com.bancolombia.acquisition.AcquisitionOperationUseCaseImpl;
import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.acquisition.AcquisitionUseCaseImpl;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCase;
import co.com.bancolombia.acquisition.AcquisitionValidateUseCaseImpl;
import co.com.bancolombia.bussinesline.BusinessLineUseCase;
import co.com.bancolombia.bussinesline.BusinessLineUseCaseImpl;
import co.com.bancolombia.catalog.CatalogUseCase;
import co.com.bancolombia.catalog.CatalogUseCaseImpl;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.checklist.CheckListUseCaseImpl;
import co.com.bancolombia.checklist.CheckListValidationUseCase;
import co.com.bancolombia.checklist.CheckListValidationUseCaseImpl;
import co.com.bancolombia.clauseacquisitionchecklist.ClauseAcquisitionChecklistUseCase;
import co.com.bancolombia.clauseacquisitionchecklist.ClauseAcquisitionChecklistUseCaseImp;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.Exceptions;
import co.com.bancolombia.customercontrol.CustomerControlUseCase;
import co.com.bancolombia.customercontrol.CustomerControlUseCaseImpl;
import co.com.bancolombia.dependentmatrixfields.DepManFieldsUseCase;
import co.com.bancolombia.dependentmatrixfields.DepManFieldsUseCaseImpl;
import co.com.bancolombia.documenttype.DocumentTypeUseCase;
import co.com.bancolombia.documenttype.DocumentTypeUseCaseImpl;
import co.com.bancolombia.execfield.ExecFieldUseCase;
import co.com.bancolombia.execfield.ExecFieldUseCaseImpl;
import co.com.bancolombia.field.FieldUseCase;
import co.com.bancolombia.field.FieldUseCaseImpl;
import co.com.bancolombia.geographiccatalog.GeographicCatalogUseCase;
import co.com.bancolombia.geographiccatalog.GeographicCatalogUseCaseImpl;
import co.com.bancolombia.instruction.InstructionClausesUseCase;
import co.com.bancolombia.instruction.InstructionClausesUseCaseImpl;
import co.com.bancolombia.instruction.InstructionSearchUseCase;
import co.com.bancolombia.instruction.InstructionSearchUseCaseImpl;
import co.com.bancolombia.instruction.InstructionUseCase;
import co.com.bancolombia.instruction.InstructionUseCaseImpl;
import co.com.bancolombia.markrevoke.MarkRevokeUseCase;
import co.com.bancolombia.markrevoke.MarkRevokeUseCaseImpl;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCase;
import co.com.bancolombia.matrixacquisition.MatrixAcquisitionUseCaseImpl;
import co.com.bancolombia.matrixinstruction.MatrixInstructionUseCase;
import co.com.bancolombia.matrixinstruction.MatrixInstructionUseCaseImpl;
import co.com.bancolombia.matrixtypeacquisition.MatrixTypeAcquisitionUseCase;
import co.com.bancolombia.matrixtypeacquisition.MatrixTypeAcquisitionUseCaseImpl;
import co.com.bancolombia.matrixtypeacquisitionclause.MatrixTypeAcquisitionClauseUseCase;
import co.com.bancolombia.matrixtypeacquisitionclause.MatrixTypeAcquisitionClauseUseCaseImpl;
import co.com.bancolombia.model.acquisition.gateways.AcquisitionRepository;
import co.com.bancolombia.model.businessline.gateways.BusinessLineRepository;
import co.com.bancolombia.model.catalog.gateways.CatalogRepository;
import co.com.bancolombia.model.checklist.gateways.CheckListRepository;
import co.com.bancolombia.model.clauseacquisitionchecklist.gateways.ClauseAcquisitionCheckListRepository;
import co.com.bancolombia.model.customercontrol.gateways.CustomerControlRepository;
import co.com.bancolombia.model.dependentfield.gateways.DependentFieldRepository;
import co.com.bancolombia.model.documenttype.gateways.DocumentTypeRepository;
import co.com.bancolombia.model.execfield.gateways.ExecFieldRepository;
import co.com.bancolombia.model.field.gateways.FieldRepository;
import co.com.bancolombia.model.geographiccatalog.gateways.GeographicCatalogRepository;
import co.com.bancolombia.model.instruction.gateways.InstructionRepository;
import co.com.bancolombia.model.markrevoke.gateways.MarkRevokeRepository;
import co.com.bancolombia.model.matrixacquisition.gateways.MatrixAcquisitionRepository;
import co.com.bancolombia.model.matrixacquisitionfield.gateways.MatrixAcquisitionFieldRepository;
import co.com.bancolombia.model.matrixinstruction.gateways.MatrixInstructionRepository;
import co.com.bancolombia.model.matrixtypeacquisition.gateways.MatrixTypeAcquisitionRepository;
import co.com.bancolombia.model.matrixtypeacquisitionclause.gateways.MatrixTypeAcquisitionClauseRepository;
import co.com.bancolombia.model.parameters.gateways.ParametersRepository;
import co.com.bancolombia.model.prerequisitesstep.gateways.PrerequisitesStepRepository;
import co.com.bancolombia.model.stateacquisition.gateways.StateAcquisitionRepository;
import co.com.bancolombia.model.statestep.gateways.StateStepRepository;
import co.com.bancolombia.model.step.gateways.StepRepository;
import co.com.bancolombia.model.typeacquisition.gateways.TypeAcquisitionRepository;
import co.com.bancolombia.model.typechannel.gateways.TypeChannelRepository;
import co.com.bancolombia.model.typeperson.gateways.TypePersonRepository;
import co.com.bancolombia.model.typeproduct.gateways.TypeProductRepository;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRepository;
import co.com.bancolombia.model.validatesession.gateways.ValidateSessionRestRepository;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.parameters.ParametersUseCaseImpl;
import co.com.bancolombia.prerequisitesstep.PrerequisitesStepUseCase;
import co.com.bancolombia.prerequisitesstep.PrerequisitesStepUseCaseImpl;
import co.com.bancolombia.stateacquisition.StateAcquisitionUseCase;
import co.com.bancolombia.stateacquisition.StateAcquisitionUseCaseImpl;
import co.com.bancolombia.statestep.StateStepUseCase;
import co.com.bancolombia.statestep.StateStepUseCaseImpl;
import co.com.bancolombia.step.StepUseCase;
import co.com.bancolombia.step.StepUseCaseImpl;
import co.com.bancolombia.typeacquisition.TypeAcquisitionUseCase;
import co.com.bancolombia.typeacquisition.TypeAcquisitionUseCaseImpl;
import co.com.bancolombia.typechannel.TypeChannelUseCase;
import co.com.bancolombia.typechannel.TypeChannelUseCaseImpl;
import co.com.bancolombia.typeperson.TypePersonUseCase;
import co.com.bancolombia.typeperson.TypePersonUseCaseImpl;
import co.com.bancolombia.typeproduct.TypeProductUseCase;
import co.com.bancolombia.typeproduct.TypeProductUseCaseImpl;
import co.com.bancolombia.util.catalog.CatalogUtilUseCase;
import co.com.bancolombia.util.catalog.CatalogUtilUseCaseImpl;
import co.com.bancolombia.validatesession.ValidateSessionUseCase;
import co.com.bancolombia.validatesession.ValidateSessionUseCaseImpl;
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
    public Exceptions exceptions() {
        return new Exceptions();
    }

    @Bean
    public AcquisitionOperationUseCase createModuleAcquisitionOperation(
            AcquisitionUseCase acquisitionUseCase, StateAcquisitionUseCase stateAcquisitionUseCase,
            @Lazy CheckListUseCase checkListUseCase, @Lazy ClauseAcquisitionChecklistUseCase checklistClauseUseCase,
            MatrixTypeAcquisitionUseCase matrixTypeAcquisitionUseCase, CatalogUtilUseCase catalogUtilUseCase,
            AcquisitionOpeValidateUseCase opeValidateUseCase, AcquisitionRepository acquisitionRepository) {
        return new AcquisitionOperationUseCaseImpl(acquisitionUseCase, stateAcquisitionUseCase, checkListUseCase,
                checklistClauseUseCase, matrixTypeAcquisitionUseCase, catalogUtilUseCase, opeValidateUseCase,
                acquisitionRepository);
    }

    @Bean
    public AcquisitionOpeValidateUseCase createModuleAcquisitionOpeValidate(
            CustomerControlUseCase customerControlUseCase, ValidateSessionUseCase validateSessionUseCase) {
        return new AcquisitionOpeValidateUseCaseImpl(customerControlUseCase, validateSessionUseCase);
    }

    @Bean
    public AcquisitionUseCase createModuleAcquisition(
            AcquisitionRepository repository, MatrixAcquisitionUseCase matrixAcquisitionUseCase,
            @Lazy DocumentTypeUseCase documentTypeUseCase) {
        return new AcquisitionUseCaseImpl(repository, matrixAcquisitionUseCase,
                documentTypeUseCase);
    }

    @Bean
    public AcquisitionValidateUseCase createModuleAcquisitionValidate(
            AcquisitionUseCase acquisitionUseCase, AcquisitionRepository repository,
            AcquisitionOperationUseCase acquisitionOperationUseCase,
            ParametersUseCase parametersUseCase, DocumentTypeUseCase documentTypeUseCase,
            @Lazy PrerequisitesStepUseCase prerequisitesStepUseCase) {
        return new AcquisitionValidateUseCaseImpl(acquisitionUseCase, repository, acquisitionOperationUseCase,
                parametersUseCase, documentTypeUseCase, prerequisitesStepUseCase);
    }

    @Bean
    public BusinessLineUseCase createModuleBusinessLine(BusinessLineRepository repository) {
        return new BusinessLineUseCaseImpl(repository);
    }

    @Bean
    public CatalogUseCase createModuleCatalogs(CatalogRepository catalogRepository) {
        return new CatalogUseCaseImpl(catalogRepository);
    }

    @Bean
    public CheckListUseCase createModuleCheckList(
            AcquisitionValidateUseCase acquisitionValidateUseCase, AcquisitionUseCase acquisitionUseCase,
            StateStepUseCase stateStepUseCase, CheckListRepository checkListRepository,
            ExecFieldUseCase execFieldUseCase, ClauseAcquisitionCheckListRepository cAcCheckListRepository) {
        return new CheckListUseCaseImpl(acquisitionValidateUseCase, acquisitionUseCase, stateStepUseCase,
                checkListRepository, execFieldUseCase, cAcCheckListRepository);
    }

    @Bean
    public CheckListValidationUseCase createModuleChecklistValidation(
            StepUseCase stepUseCase, AcquisitionUseCase acquisitionUseCase,
            CheckListRepository checkListRepository, ExecFieldUseCase execFieldUseCase) {
        return new CheckListValidationUseCaseImpl(stepUseCase, acquisitionUseCase, checkListRepository,
                execFieldUseCase);
    }

    @Bean
    public ClauseAcquisitionChecklistUseCase createModuleClauseAcquisitionChecklist(
            ClauseAcquisitionCheckListRepository repository, MatrixTypeAcquisitionClauseUseCase clauseUseCase,
            AcquisitionValidateUseCase acquisitionValidateUseCase,
            AcquisitionOperationUseCase acquisitionOperationUseCase, CoreFunctionDate coreFunctionDate,
            CheckListUseCase checkListUseCase, @Lazy MarkRevokeUseCase markRevokeUseCase) {
        return new ClauseAcquisitionChecklistUseCaseImp(repository, clauseUseCase, acquisitionValidateUseCase,
                acquisitionOperationUseCase, coreFunctionDate, checkListUseCase, markRevokeUseCase);
    }

    @Bean
    public CustomerControlUseCase createModuleCustomerControl(CustomerControlRepository customerControlRepository) {
        return new CustomerControlUseCaseImpl(customerControlRepository);
    }

    @Bean
    public DocumentTypeUseCase createModuleDocumentType(DocumentTypeRepository repository) {
        return new DocumentTypeUseCaseImpl(repository);
    }

    @Bean
    public ExecFieldUseCase createModuleExecField(ExecFieldRepository repository) {
        return new ExecFieldUseCaseImpl(repository);
    }

    @Bean
    public FieldUseCase createModuleField(FieldRepository repository) {
        return new FieldUseCaseImpl(repository);
    }

    @Bean
    public GeographicCatalogUseCase createModuleGeographicCatalog(GeographicCatalogRepository repository) {
        return new GeographicCatalogUseCaseImpl(repository);
    }

    @Bean
    public InstructionClausesUseCase createModuleInstructionClauses(
            AcquisitionValidateUseCase acquisitionValidateUseCase,
            AcquisitionOperationUseCase acquisitionOperationUseCase, CheckListUseCase checkListUseCase,
            ClauseAcquisitionCheckListRepository matrixAcquisitionUseCase,
            InstructionUseCase instructionUseCase, InstructionSearchUseCase instructionSearchUseCase) {
        return new InstructionClausesUseCaseImpl(acquisitionValidateUseCase, acquisitionOperationUseCase,
                checkListUseCase, matrixAcquisitionUseCase, instructionUseCase, instructionSearchUseCase);
    }

    @Bean
    public InstructionSearchUseCase createModuleInstructionSearch(
            AcquisitionOperationUseCase acquisitionOperationUseCase, MatrixAcquisitionUseCase matrixAcquisitionUseCase,
            MatrixInstructionUseCase matrixInstructionUseCase, CheckListUseCase checkListUseCase,
            InstructionUseCase instructionUseCase) {
        return new InstructionSearchUseCaseImpl(acquisitionOperationUseCase, matrixAcquisitionUseCase,
                matrixInstructionUseCase, checkListUseCase, instructionUseCase);
    }

    @Bean
    public InstructionUseCase createModuleInstruction(InstructionRepository repository) {
        return new InstructionUseCaseImpl(repository);
    }

    @Bean
    public MarkRevokeUseCase createModuleCaseMarkRevoke(MarkRevokeRepository repository) {
        return new MarkRevokeUseCaseImpl(repository);
    }

    @Bean
    public MatrixAcquisitionUseCase createModuleMatrixAcquisition(MatrixAcquisitionRepository repository) {
        return new MatrixAcquisitionUseCaseImpl(repository);
    }

    @Bean
    public MatrixInstructionUseCase createModuleMatrixInstruction(MatrixInstructionRepository repository) {
        return new MatrixInstructionUseCaseImpl(repository);
    }

    @Bean
    public MatrixTypeAcquisitionUseCase createModuleMatrixTypeAcquisition(MatrixTypeAcquisitionRepository repository) {
        return new MatrixTypeAcquisitionUseCaseImpl(repository);
    }

    @Bean
    public MatrixTypeAcquisitionClauseUseCase createModuleMatrixTypeAcquisitionClause(
            MatrixTypeAcquisitionClauseRepository repository) {
        return new MatrixTypeAcquisitionClauseUseCaseImpl(repository);
    }

    @Bean
    public ParametersUseCase createModuleParameters(ParametersRepository repository) {
        return new ParametersUseCaseImpl(repository);
    }

    @Bean
    public PrerequisitesStepUseCase createModulePrerequisitesStep(
            PrerequisitesStepRepository prerequisitesStepRepository, StepUseCase stepUseCase,
            CheckListUseCase checkListUseCase) {
        return new PrerequisitesStepUseCaseImpl(prerequisitesStepRepository, stepUseCase, checkListUseCase);
    }

    @Bean
    public StateAcquisitionUseCase createModuleStateAcquisition(StateAcquisitionRepository repository) {
        return new StateAcquisitionUseCaseImpl(repository);
    }

    @Bean
    public StateStepUseCase createModuleStateStep(StateStepRepository repository) {
        return new StateStepUseCaseImpl(repository);
    }

    @Bean
    public StepUseCase createModuleStep(StepRepository repository) {
        return new StepUseCaseImpl(repository);
    }

    @Bean
    public TypeAcquisitionUseCase createModuleTypeAcquisition(TypeAcquisitionRepository repository) {
        return new TypeAcquisitionUseCaseImpl(repository);
    }

    @Bean
    public TypeChannelUseCase createModuleTypeChannel(TypeChannelRepository repository) {
        return new TypeChannelUseCaseImpl(repository);
    }

    @Bean
    public TypePersonUseCase createModuleTypePerson(TypePersonRepository repository) {
        return new TypePersonUseCaseImpl(repository);
    }

    @Bean
    public TypeProductUseCase createModuleTypeProduct(TypeProductRepository repository) {
        return new TypeProductUseCaseImpl(repository);
    }

    @Bean
    public CatalogUtilUseCase createModuleCatalogUtil(
            DocumentTypeUseCase documentTypeUseCase, TypeChannelUseCase typeChannelUseCase,
            TypeProductUseCase typeProductUseCase, BusinessLineUseCase businessLineUseCase) {
        return new CatalogUtilUseCaseImpl(businessLineUseCase, typeChannelUseCase, documentTypeUseCase,
                typeProductUseCase);
    }

    @Bean
    public ValidateSessionUseCase createModuleValidateSession(
            DocumentTypeUseCase documentTypeUseCase, ValidateSessionRestRepository validateSessionRestRepository,
            ValidateSessionRepository validateSessionRepository) {
        return new ValidateSessionUseCaseImpl(documentTypeUseCase, validateSessionRestRepository,
                validateSessionRepository);
    }

    @Bean
    public DepManFieldsUseCase depManFieldsUseCase(
            MatrixAcquisitionUseCase matAcqUseCase, StepUseCase stepUseCase, MatrixAcquisitionFieldRepository mafr,
            DependentFieldRepository dependentFieldRepository, Exceptions exceptions){
        return new DepManFieldsUseCaseImpl(matAcqUseCase, stepUseCase, mafr, dependentFieldRepository, exceptions);
    }
}