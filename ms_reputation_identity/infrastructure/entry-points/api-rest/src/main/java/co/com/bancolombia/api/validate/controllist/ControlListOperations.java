package co.com.bancolombia.api.validate.controllist;

import co.com.bancolombia.api.model.ControlListResponse;
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

public interface ControlListOperations {

    @ApiOperation(value = "Validate Customer", nickname = "validateCustomerControlList",
            notes = "Validate  Customer Control List Operation", response = ControlListResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionValidate",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = ControlListResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/validate-control-lists", produces = {"application/json"},
            consumes = {"application/json"}, method = RequestMethod.POST)
    ResponseEntity<ControlListResponse> validateCustomerControlList(@ApiParam(value = "Information related to " +
            "validate customer List Control", required = true) @Valid @RequestBody UserInfoRequest body
    );
}
