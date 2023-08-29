package co.com.bancolombia.contactinformation;

import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.Error;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.contactinformation.contactinfo.ContactInfoRequest;
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

public interface ContactOperations {

    @ApiOperation(value = "Contact Customer information", nickname = "contactInformation",
            notes = "Contact Customer information operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionInformation",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = CodeNameResponse.class), @ApiResponse(code = 400, message = "Wrong parameters or bad request",
            response = Error.class)})
    @RequestMapping(value = "/contact-information", produces = {"application/json"}, consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<CodeNameResponse> contactInformation(
            @ApiParam(value = "Information related to contact customer", required = true) @Validated(
                    {ValidationMandatory.class}) @RequestBody ContactInfoRequest body
    );
}
