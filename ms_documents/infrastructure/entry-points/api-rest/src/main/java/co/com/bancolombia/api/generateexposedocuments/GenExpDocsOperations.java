package co.com.bancolombia.api.generateexposedocuments;

import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsRequest;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsResponse;
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

public interface GenExpDocsOperations {
    @ApiOperation(value = "Generate - expose documents", nickname = "gen-exp-documents",
            notes = "generate - expose documents", response = GenExpDocsResponse.class,
            authorizations = { @Authorization(value = "ApiKeyAuth") }, tags = { "AcquisitionDocuments", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = GenExpDocsResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class) })
    @RequestMapping(value = "/generate-expose-documents", produces = {"application/json"},
            consumes = {"application/json" }, method = RequestMethod.POST)
    ResponseEntity<GenExpDocsResponse> generateExposeDocuments(
            @ApiParam(value = "Information related to generate - expose documents", required = true)
            @Validated({ValidationMandatory.class }) @RequestBody GenExpDocsRequest body);
}