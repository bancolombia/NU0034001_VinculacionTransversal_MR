package co.com.bancolombia.api.health;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface HealthOperations {

    @ApiOperation(value = "Test if the api is alive", nickname = "health",
            notes = "Health operation", response = String.class, tags = {"AcquisitionDocuments",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully executed", response = String.class),
            @ApiResponse(code = 404, message = "Wrong parameters or bad" + " request", response = Error.class)})
    @RequestMapping(value = "/health", produces = {"text/html"}, method = RequestMethod.GET)
    ResponseEntity<String> health();
}
