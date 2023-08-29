package co.com.bancolombia.taxinformation;

import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.taxinformation.model.TaxInfoRequest;
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

public interface TaxOperations {
    @ApiOperation(value = "Tax Customer information", nickname = "taxInformation",
            notes = "Tax Customer information operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionInformation",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = CodeNameResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/tax-information", produces = {"application/json"},
            consumes = {"application/json"}, method = RequestMethod.POST)
    ResponseEntity<CodeNameResponse> taxInformation(
            @ApiParam(value = "Information related to tax customer", required = true)
            @Validated({ValidationMandatory.class}) @RequestBody TaxInfoRequest body
    );
}