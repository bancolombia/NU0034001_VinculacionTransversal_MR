package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.api.model.acquisition.AcquisitionRequest;
import co.com.bancolombia.api.model.acquisition.AcquisitionResponse;
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

public interface AcquisitionOperations {
    @ApiOperation(value = "Performs the Acquisition operations", nickname = "startAcquisition",
            notes = "Start Acquisition Operation", response = AcquisitionResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = AcquisitionResponse.class), @ApiResponse(code = 400,
            message = "Wrong " + "parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/start-acquisition", produces = {"application/json"}, consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<AcquisitionResponse> startAcquisition(
            @ApiParam(value = "Information related " + "to the user customer",
                    required = true) @Valid @RequestBody AcquisitionRequest body
    );

    @ApiOperation(value = "Test if the api is alive", nickname = "health", notes = "Health " + "operation",
            response = String.class, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = String.class), @ApiResponse(code = 404, message = "Wrong parameters or bad" + " request",
            response = Error.class)})
    @RequestMapping(value = "/health", produces = {"text/html"}, method = RequestMethod.GET)
    public ResponseEntity<String> health();
}
