package co.com.bancolombia.api.identity;

import co.com.bancolombia.api.model.ValidateQuestionResponse;
import co.com.bancolombia.api.model.ValidateQuestionsRequest;
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

public interface ValidateQuestionsOperations {

    @ApiOperation(value = "Validate Challenge Questions Customer", nickname = "validateQuestions",
            notes = "Validate Questions operation", response = ValidateQuestionResponse.class,
            authorizations = {
                    @Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionIdentity",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = ValidateQuestionResponse.class),
            @ApiResponse(code = 400, message = "Wrong parameters or bad request", response = Error.class)})
    @RequestMapping(value = "/validate-questions", produces = {"application/json"}, consumes = {
            "application/json"}, method = RequestMethod.POST)
    ResponseEntity<ValidateQuestionResponse> validateQuestionsIdentity(
            @ApiParam(value = "Validate questions identity customer", required = true) @Validated({
                    ValidationMandatory.class}) @RequestBody ValidateQuestionsRequest body);
}
