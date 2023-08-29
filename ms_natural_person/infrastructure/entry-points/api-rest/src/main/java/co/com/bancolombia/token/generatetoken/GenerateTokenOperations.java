package co.com.bancolombia.token.generatetoken;

import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequest;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenResponse;
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

import java.text.ParseException;

public interface GenerateTokenOperations {

	/*
	 * GENERATE TOKEN
	 */
	@ApiOperation(value = "Generate token ", nickname = "generateToken", notes = "Generate token operation", 
			response = GenerateTokenResponse.class, authorizations = {
			@Authorization(value = "ApiKeyAuth") }, tags = { "AcquisitionValidate", })
	@ApiResponses(value = {
			@ApiResponse(
					code = 200, message = "Operation successfully executed",
					response = GenerateTokenResponse.class),
			@ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class) })
	@RequestMapping(value = "/generate-token", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<GenerateTokenResponse> generateToken(
			@ApiParam(value = "Information related to generate token", required = true) @Validated({
					ValidationMandatory.class }) @RequestBody GenerateTokenRequest body)
			throws ParseException;
}
