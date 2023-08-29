package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequest;

import co.com.bancolombia.api.model.customerdocumentpersistence.PersistenceDocumentResponse;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface CustomerDocumentPersistenceOperations {

    @ApiOperation(value = "Customer Document Persistence", nickname = "customer-document-persistence",
            notes = "customer document persistence", response = PersistenceDocumentResponse.class, authorizations = {
            @Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionPersistence",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = CodeNameResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/customer-document-persistence", produces = {"application/json"}, consumes = {
            "application/json"}, method = RequestMethod.POST)
    public ResponseEntity<PersistenceDocumentResponse> customerDocumentPersistence(
            @ApiParam(value = "Information related to customer document persistence", required = true) @Validated({
                    ValidationMandatory.class}) @RequestBody CustomerPersistenceDocumentRequest body);

}