package co.com.bancolombia.api.retrievescenario;

import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface RetrieveScenarioOperations {

    @ApiOperation(value = "Retrieve Scenario information", nickname = "retrieveScenario",
            notes = "Retrieve Scenario information operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = CodeNameResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/retrieve-scenario", produces = {"application/json"}, consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<CodeNameResponse> retrieveScenario(
            @ApiParam(value = "Information related retrieve scenario", required = true)
            @Validated @RequestBody UserInfoRequest body);
}
