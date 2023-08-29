package co.com.bancolombia.api.clauseinstructions;

import co.com.bancolombia.VinUpdateController;
import co.com.bancolombia.api.model.acceptclauses.AcceptClausesRequest;
import co.com.bancolombia.api.model.clauseinstructions.InstructionsResponse;
import co.com.bancolombia.api.response.ResponseFactory;
import co.com.bancolombia.clauseacquisitionchecklist.ClauseAcquisitionChecklistUseCase;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.instruction.InstructionClausesUseCase;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import co.com.bancolombia.model.acquisition.BasicAcquisitionRequest;
import co.com.bancolombia.model.acquisition.ClauseInstructions;
import co.com.bancolombia.model.acquisition.ClauseInstructionsWithAcquisition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@VinUpdateController
@Api(tags = {"AcquisitionManagement"})
public class ClauseInstructionsController implements ClauseInstructionsOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private ClauseAcquisitionChecklistUseCase clauseAcquisitionUseCase;

    @Autowired
    private InstructionClausesUseCase instructionUseCase;

    @Autowired
    private ILogFuncAcquisitionUseCase acquisitionUseCase;

    @Autowired
    private ILogFuncCheckListUseCase checkListUseCase;

    @Autowired
    private ILogFuncFieldUseCase fieldUseCase;

    @Autowired
    private CoreFunctionDate coreFunctionDate;

    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = Constants.CODE_CONSULT_INSTRUCTIONS)
    public ResponseEntity<InstructionsResponse> consultInstructions(
            @ApiParam(value = "Information related to consult instructions", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody UserInfoRequest body) {

        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().webRequest(webRequest)
                .acquisitionUseCase(acquisitionUseCase).fieldUseCase(fieldUseCase).coreFunctionDate(coreFunctionDate)
                .checkListUseCase(checkListUseCase).build();

        UUID acqId = stepForLogFunctional.firstStepForLogFunctional(Constants.CODE_CONSULT_INSTRUCTIONS,
                AcquisitionInitial.builder().meta(body.getMeta())
                        .data(MyDataInitial.builder().documentNumber(body.getData().getDocumentNumber())
                                .acquisitionId(body.getData().getAcquisitionId())
                                .documentType(body.getData().getDocumentType()).build())
                        .build());

        ClauseInstructionsWithAcquisition ret = this.instructionUseCase.searchClausesAndInstructions(
                body.getData().getAcquisitionId(), body.getData().getDocumentType(),
                body.getData().getDocumentNumber(), Constants.CODE_CONSULT_INSTRUCTIONS);

        ClauseInstructions result = ClauseInstructions.builder().build();
        if (ret != null) {
            result = ret.getClauseInstructions();
        }

        if (acqId != null || body.getData().getAcquisitionId() != null) {
            if (acqId == null) {
                acqId = UUID.fromString(body.getData().getAcquisitionId());
            }
            stepForLogFunctional.finallyStepForLogFunctional(StepForLogClass.builder().idAcquisition(acqId)
                    .operation(Constants.CODE_CONSULT_INSTRUCTIONS).build());
        }

        return new ResponseEntity<>(ResponseFactory.buildConsultInstructionsResponse(body, result), HttpStatus.OK);
    }

    @ILogRegister(api = Constants.API_CUSTOMER_VALUE, operation = Constants.CODE_ACCEPT_CLAUSES)
    public ResponseEntity<CodeNameResponse> acceptClauses(
            @ApiParam(value = "Information related to accept clauses", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody AcceptClausesRequest body) {

        StepForLogFunctional stepForLogFunctional = StepForLogFunctional.builder().webRequest(webRequest)
                .acquisitionUseCase(acquisitionUseCase).fieldUseCase(fieldUseCase).coreFunctionDate(coreFunctionDate)
                .checkListUseCase(checkListUseCase).build();

        UUID acqId = stepForLogFunctional.firstStepForLogFunctional(Constants.CODE_ACCEPT_CLAUSES,
                AcquisitionInitial.builder().meta(body.getMeta())
                        .data(MyDataInitial.builder().documentNumber(body.getData().getDocumentNumber())
                                .acquisitionId(body.getData().getAcquisitionId())
                                .documentType(body.getData().getDocumentType()).build())
                        .build());

        BasicAcquisitionRequest request = BasicAcquisitionRequest.builder().idAcq(body.getData().getAcquisitionId())
                .documentType(body.getData().getDocumentType()).documentNumber(body.getData().getDocumentNumber())
                .userTransaction(body.getMeta().getUsrMod()).build();

        Acquisition acqInstance = clauseAcquisitionUseCase.acceptClause(request, body.getData().getAcceptClauses(),
                body.getData().getClauseCode(), Constants.CODE_ACCEPT_CLAUSES);

        if (acqInstance != null) {
            acqId = acqInstance.getId();
        }

        if (acqId != null || body.getData().getAcquisitionId() != null) {
            if (acqId == null) {
                acqId = UUID.fromString(body.getData().getAcquisitionId());
            }

            stepForLogFunctional.finallyStepForLogFunctional(
                    StepForLogClass.builder().idAcquisition(acqId).operation(Constants.CODE_ACCEPT_CLAUSES).build());
        }

        return new ResponseEntity<>(ResponseFactory.buildCodeNameResponse(body.getMeta()), HttpStatus.OK);
    }

}
