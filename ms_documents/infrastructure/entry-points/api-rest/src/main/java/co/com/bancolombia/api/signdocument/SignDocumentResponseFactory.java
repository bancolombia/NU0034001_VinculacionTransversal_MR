package co.com.bancolombia.api.signdocument;

import co.com.bancolombia.api.model.signdocument.SDRequest;
import co.com.bancolombia.api.model.signdocument.SDResponse;
import co.com.bancolombia.api.model.signdocument.SDResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SD_RESPONSE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SD_RESPONSE_NAME;

public class SignDocumentResponseFactory {

    private SignDocumentResponseFactory() {
    }

    public static SDResponse buildSignDocumentResponse(SDRequest request){
        SDResponseData data = SDResponseData.builder().responseCode(SD_RESPONSE_CODE).responseName(SD_RESPONSE_NAME)
                .build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return SDResponse.builder().data(data).meta(metaResponse).build();
    }
}
