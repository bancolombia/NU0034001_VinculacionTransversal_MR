package co.com.bancolombia.api.health;

import co.com.bancolombia.DocumentsController;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DocumentsController
@Api(tags = {"AcquisitionDocuments",})
public class HealthController implements HealthOperations {

    @Override
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Customer", HttpStatus.OK);
    }
}
