package co.com.bancolombia.api.validate.markcustomer;

import co.com.bancolombia.api.model.markcustomer.MarkCustomerResponse;
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

public interface MarkCustomerOperations {

    @ApiOperation(value = "Mark Customer", nickname = "mark-customer", notes = "mark customer",
            response = MarkCustomerResponse.class, authorizations = {
            @Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionValidate",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = MarkCustomerResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/mark-customer", produces = {"application/json"}, consumes = {
            "application/json"}, method = RequestMethod.POST)
    public ResponseEntity<MarkCustomerResponse> markCustomer(
            @ApiParam(value = "Information related to mark customer", required = true) @Validated({
                    ValidationMandatory.class}) @RequestBody UserInfoRequest body);
}
