package co.com.bancolombia.api.uploaddocument;

import co.com.bancolombia.api.model.uploaddocument.UploadDocumentApiResponse;
import co.com.bancolombia.api.model.uploaddocument.UploadDocumentRequest;
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

public interface UploadDocumentOperations {
    @ApiOperation(value = "Documentation Customer", nickname = "uploaddocument",
            notes = "Upload Document Operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionDocuments",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed",
                    response = UploadDocumentApiResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/upload-document", produces = {"application/json"}, consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<UploadDocumentApiResponse> uploadDocument(
            @ApiParam(value = "Information related to upload document", required = true)
            @Validated({ValidationMandatory.class}) @RequestBody UploadDocumentRequest body
    );
}
