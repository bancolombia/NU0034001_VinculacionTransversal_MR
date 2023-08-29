package co.com.bancolombia.basicinformation;

import co.com.bancolombia.basicinformation.basicinfo.BasicInfoRequest;
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

public interface BasicInformationOperations {
    @ApiOperation(value = "Basic Customer information", nickname = "basicInformation",
        notes = "Basic Customer information operation", response = CodeNameResponse.class,
        authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionInformation",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
        response = CodeNameResponse.class), @ApiResponse(code = 400, message = "Wrong parameters or bad request",
        response = Error.class)})
    @RequestMapping(value = "/basic-information", produces = {"application/json"}, consumes = {"application/json"},
        method = RequestMethod.POST)
    ResponseEntity<CodeNameResponse> basicInformation(
            @ApiParam(value = "Information related to basic customer", required = true) @Validated(
                    {ValidationMandatory.class}) @RequestBody BasicInfoRequest body
    );

    @ApiOperation(value = "Test if the api is alive", nickname = "health", notes = "Health " + "operation",
            response = String.class, tags = {"AcquisitionManagement",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = String.class),
            @ApiResponse(code = 404, message = "Wrong parameters or bad" + " request", response = Error.class)})
    @RequestMapping(value = "/health", produces = {"text/html"}, method = RequestMethod.GET)
    ResponseEntity<String> health();
}