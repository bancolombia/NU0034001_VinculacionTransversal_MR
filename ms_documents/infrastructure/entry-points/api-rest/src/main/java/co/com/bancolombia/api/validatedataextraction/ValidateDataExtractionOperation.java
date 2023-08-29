package co.com.bancolombia.api.validatedataextraction;

import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionRequest;
import co.com.bancolombia.api.model.validatedataextraction.ValidateDataExtractionResponse;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
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

public interface ValidateDataExtractionOperation {
	@ApiOperation(value = "Documentation Customer", nickname = "validatedataextraction", 
			notes = "Validate Data Extraction Operation", response = CodeNameResponse.class, authorizations = {
			@Authorization(value = "ApiKeyAuth") }, tags = { "AcquisitionUploadDocument", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operation successfully executed",
					response = ValidateDataExtractionResponse.class),
			@ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class) })
	@RequestMapping(value = "/validate-data-extraction", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ValidateDataExtractionResponse> validateDataExtraction(
			@ApiParam(value = "Information related to Validate Data Extraction", required = true) @Validated({
					ValidationMandatory.class, ValidationAcquisitionId.class })
			@RequestBody ValidateDataExtractionRequest body);

}
