package co.com.bancolombia.foreigninformation;

import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequest;
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

public interface ForeignOperations {

    @ApiOperation(value = "Foreign Currency information", nickname = "foreignCurrencyInformation",
            notes = "Foreign Currency information operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionInformation",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = CodeNameResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/foreign-currency-info", produces = {"application/json"},
            consumes = {"application/json"}, method = RequestMethod.POST)
    ResponseEntity<CodeNameResponse> foreignCurrencyInformation(
            @ApiParam(value = "Information related to foreign currency information", required = true)
            @Validated({ValidationMandatory.class})
            @RequestBody ForeignCurrencyInfoRequest body
    );
}