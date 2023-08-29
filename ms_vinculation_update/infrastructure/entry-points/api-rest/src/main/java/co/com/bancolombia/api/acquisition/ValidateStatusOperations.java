package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.api.model.validatestatus.ValidateStatusResponse;
import co.com.bancolombia.commonsvnt.api.model.util.Error;
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

public interface ValidateStatusOperations {

    @ApiOperation(value = "Validate Status Acquisition", nickname = "validateStatus",
        notes = "Validate Status Operation", response = ValidateStatusResponse.class,
        authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed",
                    response = ValidateStatusResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/validate-status", produces = {"application/json"}, consumes = {"application/json"},
        method = RequestMethod.POST)
    public ResponseEntity<ValidateStatusResponse> validateStatus(
        @ApiParam(value = "Information related to validate status", required = true)
        @Validated({ValidationMandatory.class}) @RequestBody UserInfoRequest body);
}
