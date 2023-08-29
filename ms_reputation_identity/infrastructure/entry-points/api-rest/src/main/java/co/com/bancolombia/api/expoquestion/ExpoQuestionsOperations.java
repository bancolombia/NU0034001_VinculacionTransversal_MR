package co.com.bancolombia.api.expoquestion;

import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsRequest;
import co.com.bancolombia.api.model.expoquestion.ExpoQuestionsResponse;
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

public interface ExpoQuestionsOperations {
    @ApiOperation(value = "Expose Challenge Questions Customer", nickname = "expoQuestions",
            notes = "Expo Questions operation", response = CodeNameResponse.class,
            authorizations = {@Authorization(value = "ApiKeyAuth")}, tags = {"AcquisitionIdentity",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operation successfully executed",
            response = CodeNameResponse.class), @ApiResponse(code = 400, message = "Wrong parameters or bad request",
            response = Error.class)})
    @RequestMapping(value = "/expo-questions", produces = {"application/json"}, consumes = {
            "application/json"}, method = RequestMethod.POST)
    ResponseEntity<ExpoQuestionsResponse> expoQuestionsIdentity(
            @ApiParam(value = "/customer/expo-questions", required = true) @Validated({
                    ValidationMandatory.class}) @RequestBody ExpoQuestionsRequest body);
}
