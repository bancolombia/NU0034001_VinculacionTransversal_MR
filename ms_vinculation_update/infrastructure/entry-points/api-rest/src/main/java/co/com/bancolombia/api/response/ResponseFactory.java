package co.com.bancolombia.api.response;

import co.com.bancolombia.api.model.acquisition.AcquisitionRequest;
import co.com.bancolombia.api.model.acquisition.AcquisitionResponse;
import co.com.bancolombia.api.model.acquisition.AcquisitionResponseData;
import co.com.bancolombia.api.model.acquisition.UpdateRequest;
import co.com.bancolombia.api.model.clauseinstructions.Instruction;
import co.com.bancolombia.api.model.clauseinstructions.InstructionsActionsResponseData;
import co.com.bancolombia.api.model.clauseinstructions.InstructionsClausesResponseData;
import co.com.bancolombia.api.model.clauseinstructions.InstructionsResponse;
import co.com.bancolombia.api.model.clauseinstructions.InstructionsResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.acquisition.ClauseInstructions;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATA_ADMINISTRATION_POLICY_URL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT_TIME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.INFORMATION_PROCCESING_URL;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATUS_OK;

public class ResponseFactory {

    private ResponseFactory() {
    }

    public static AcquisitionResponse buildStartAcquisitionResponse(
            AcquisitionRequest request, Acquisition acquisition
    ) {
        AcquisitionResponseData data = AcquisitionResponseData.builder()
                .acquisitionId(acquisition.getId().toString())
                .documentType(acquisition.getDocumentType().getCode())
                .documentNumber(acquisition.getDocumentNumber())
                .registrationDate(new SimpleDateFormat(DATE_FORMAT_TIME).format(acquisition.getCreatedDate()))
                .codeStateAcquisition(acquisition.getStateAcquisition().getCode())
                .nameStateAcquisition(acquisition.getStateAcquisition().getName())
                .build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return AcquisitionResponse.builder().data(data).meta(metaResponse).build();
    }

    public static AcquisitionResponse buildStartUpdateResponse(
            UpdateRequest request, Acquisition acquisition
    ) {
        AcquisitionResponseData data = AcquisitionResponseData.builder()
                .acquisitionId(acquisition.getId().toString())
                .documentType(acquisition.getDocumentType().getCode())
                .documentNumber(acquisition.getDocumentNumber())
                .registrationDate(new SimpleDateFormat(DATE_FORMAT_TIME).format(acquisition.getCreatedDate()))
                .codeStateAcquisition(acquisition.getStateAcquisition().getCode())
                .nameStateAcquisition(acquisition.getStateAcquisition().getName())
                .build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return AcquisitionResponse.builder().data(data).meta(metaResponse).build();
    }

    public static CodeNameResponse buildCodeNameResponse(MetaRequest request) {
        CodeNameResponseData data = CodeNameResponseData.builder().code(CODE_OK)
                .name(STATUS_OK).build();
        return CodeNameResponse.builder().data(data).meta(MetaResponse.fromMeta(request)).build();
    }

    public static InstructionsResponse buildConsultInstructionsResponse(
            UserInfoRequest request, ClauseInstructions clauseInstructions) {
        List<Instruction> retInstructions = clauseInstructions.getInstructions()
                .stream().map(instruction -> Instruction.builder().code(instruction.getInstruction().getCode())
                        .name(instruction.getInstruction().getName()).order(instruction.getSequence())
                        .stepCode(instruction.getMatrixAcquisition().getStep().getCode()).stepName(instruction
                                .getMatrixAcquisition().getStep().getName()).build()).collect(Collectors.toList());
        List<InstructionsClausesResponseData> retClauses = clauseInstructions.getClauses().stream().map(
                clauses -> InstructionsClausesResponseData.builder().code(clauses.getClause().getCode())
                        .name(clauses.getClause().getName()).order(clauses.getSequence())
                        .stepCode(clauses.getStep().getCode()).stepName(clauses.getStep().getName())
                        .actionsList(clauses.getContainerActions().stream()
                                .filter(Objects::nonNull).map(
                                        actions -> InstructionsActionsResponseData.builder().actionCode(
                                                actions.getCode()).actionName(actions.getName()).build())
                                .collect(Collectors.toList())).build()).collect(Collectors.toList());

        InstructionsResponseData data = InstructionsResponseData.builder()
                .instructionsList(retInstructions).clausesList(retClauses)
                .informationProcessingUrl(INFORMATION_PROCCESING_URL)
                .dataAdministrationPolicyUrl(DATA_ADMINISTRATION_POLICY_URL).build();
        return InstructionsResponse.builder().data(data).meta(MetaResponse.fromMeta(request.getMeta())).build();
    }
}