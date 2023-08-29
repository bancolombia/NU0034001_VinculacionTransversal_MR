package co.com.bancolombia.api.customerdocumentpersistence;

import co.com.bancolombia.api.model.customerdocumentpersistence.DocumentPersistenceRetriesRequest;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public interface PersistenceDocumentRetriesOperation {

    @RequestMapping(value = "/document-persistence-retries",
            consumes = {"application/json"}, method = RequestMethod.POST)
    void persistenceDocumentOperation(
            @ApiParam(value = "Information related to " + "Persistence Document", required = true)
            @Valid @RequestBody DocumentPersistenceRetriesRequest body
    );
}
