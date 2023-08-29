package co.com.bancolombia.api.identity;

import co.com.bancolombia.api.model.ValidateIdentityRequest;
import co.com.bancolombia.api.model.ValidateIdentityResponse;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
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

public interface IdentityOperations {

    @ApiOperation(value = "Validate Customer Identity", nickname = "validateIdentity",
            notes = "Validate Customer Identity operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionIdentity",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = CodeNameResponse.class), @ApiResponse(code = 400, message = "Wrong parameters or bad request",
            response = Error.class)})
    @RequestMapping(value = "/validate-identity", produces = {"application/json"}, consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<ValidateIdentityResponse> validateIdentity(
            @ApiParam(value = "Validate identity customer", required = true) @Validated(
                    {ValidationMandatory.class}) @RequestBody ValidateIdentityRequest body
    );

    @ApiOperation(value = "Test if the api is alive", nickname = "health", notes = "Health " + "operation",
            response = String.class, tags = {"AcquisitionIdentity",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = String.class), @ApiResponse(code = 404, message = "Wrong parameters or bad" + " request",
            response = Error.class)})
    @RequestMapping(value = "/health", produces = {"text/html"}, method = RequestMethod.GET)
    public ResponseEntity<String> health();
}

