package co.com.bancolombia.api.clauseinstructions;

import co.com.bancolombia.api.model.acceptclauses.AcceptClausesRequest;
import co.com.bancolombia.api.model.clauseinstructions.InstructionsResponse;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.Error;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public interface ClauseInstructionsOperations {

    @ApiOperation(value = "Retrieves the process instructions", nickname = "consultInstructions",
        notes = "Consult Instructions Operation", response = InstructionsResponse.class,
        authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Operation successfully executed", response = InstructionsResponse.class),
        @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class) })
    @RequestMapping(value = "/consult-instructions", produces = {"application/json"}, consumes = {"application/json"},
        method = RequestMethod.POST)
    ResponseEntity<InstructionsResponse> consultInstructions(
        @ApiParam(value = "Information related to consult instructions", required = true)
        @Valid @RequestBody UserInfoRequest body);

    @ApiOperation(value = "Acceptance of clauses of the process", nickname = "acceptClauses",
        notes = "Accept Clauses Operation", response = CodeNameResponse.class,
        authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Operation successfully executed", response = CodeNameResponse.class),
        @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/accept-clauses", produces = {"application/json"}, consumes = {"application/json"},
        method = RequestMethod.POST)
    ResponseEntity<CodeNameResponse> acceptClauses(
        @ApiParam(value = "Information related to accept clauses", required = true)
        @Valid @RequestBody AcceptClausesRequest body);
}
