package co.com.bancolombia.response;


import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponse;
import co.com.bancolombia.commonsvnt.api.model.util.CodeNameResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_OK;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.STATUS_OK;

public class ResponseFactory {

    private ResponseFactory() {
    }

    public static CodeNameResponse buildCodeNameResponse(MetaRequest request) {
        CodeNameResponseData data = CodeNameResponseData.builder().code(CODE_OK)
                .name(STATUS_OK).build();
        return CodeNameResponse.builder().data(data).meta(MetaResponse.fromMeta(request)).build();
    }
}