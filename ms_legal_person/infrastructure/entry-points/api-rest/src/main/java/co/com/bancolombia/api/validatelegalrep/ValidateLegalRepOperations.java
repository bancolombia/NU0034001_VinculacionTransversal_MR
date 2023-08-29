package co.com.bancolombia.api.validatelegalrep;

import co.com.bancolombia.api.model.util.Error;
import co.com.bancolombia.api.model.validatelegalrep.ValidateLegalRepResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
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

public interface ValidateLegalRepOperations {

    @ApiOperation(value = "Validate Legal Representative", nickname = "validateLegalRepresentative",
            notes = "Validate Legal Representative Operation", response = ValidateLegalRepResponse.class,
            authorizations = { @Authorization(value = "ApiKeyAuth") }, tags = { "AcquisitionValidation", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed",
                    response = ValidateLegalRepResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class) })
    @RequestMapping(value = "/validate-legal-representative", produces = { "application/json" },
            consumes = { "application/json" }, method = RequestMethod.POST)
    ResponseEntity<ValidateLegalRepResponse> validateLegalRep(
            @ApiParam(value = "Information related to the customer's legal representative", required = true)
            @Validated({ValidationMandatory.class}) @RequestBody UserInfoRequest body);
}
