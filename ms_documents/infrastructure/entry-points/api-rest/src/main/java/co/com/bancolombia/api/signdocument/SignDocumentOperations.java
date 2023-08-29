package co.com.bancolombia.api.signdocument;

import co.com.bancolombia.api.model.signdocument.SDRequest;
import co.com.bancolombia.api.model.signdocument.SDResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public interface SignDocumentOperations {

    @ApiOperation(value = "Sign Document", nickname = "sign-document", notes = "sign document",
            response = SDResponse.class, authorizations = {
            @Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionDocuments",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = SDResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/sign-document", produces = {"application/json"}, consumes = {
            "application/json"}, method = RequestMethod.POST)
    ResponseEntity<SDResponse> signDocument(
            @ApiParam(value = "Information related to sign document", required = true)
            @Validated({ValidationMandatory.class}) @RequestBody SDRequest body) throws IOException, MessagingException;
}


