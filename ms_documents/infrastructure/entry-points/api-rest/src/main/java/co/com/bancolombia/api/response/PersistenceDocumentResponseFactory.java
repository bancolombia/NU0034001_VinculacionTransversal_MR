package co.com.bancolombia.api.response;

import co.com.bancolombia.api.model.customerdocumentpersistence.CustomerPersistenceDocumentRequest;
import co.com.bancolombia.api.model.customerdocumentpersistence.PersistenceDocumentResponse;
import co.com.bancolombia.api.model.customerdocumentpersistence.PersistenceDocumentResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_CODE_ZERO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.OUT_COME_NAME_ZERO;

public class PersistenceDocumentResponseFactory {

    private PersistenceDocumentResponseFactory() {
    }

    public static PersistenceDocumentResponse buildPersistenceDocumentResponse(
            CustomerPersistenceDocumentRequest request) {
        PersistenceDocumentResponseData responseData = PersistenceDocumentResponseData.builder()
                .answerCode(OUT_COME_CODE_ZERO).answerName(OUT_COME_NAME_ZERO).build();

        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return PersistenceDocumentResponse.builder().data(responseData).meta(metaResponse).build();
    }
}
