package co.com.bancolombia.token.validatetoken;

import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenResponse;
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

public interface ValidateTokenOperations {

	/*
	 * VALIDATE TOKEN
	 */
	@ApiOperation(value = "Validate token ", nickname = "validateToken", notes = "Validate token operation", 
			response = CodeNameResponse.class, authorizations = {
			@Authorization(value = "ApiKeyAuth") }, tags = { "AcquisitionValidate", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operation successfully executed", response = CodeNameResponse.class),
			@ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class) })
	@RequestMapping(value = "/validate-token", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<ValidateTokenResponse> validateToken(
			@ApiParam(value = "Information related to validate token", required = true) @Validated({
					ValidationMandatory.class }) @RequestBody ValidateTokenRequest body);
}
