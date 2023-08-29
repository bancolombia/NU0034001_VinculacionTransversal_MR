package co.com.bancolombia;

import co.com.bancolombia.api.model.ControlListResponse;
import co.com.bancolombia.api.model.ControlListResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.model.controllist.ControlList;

public class ResponseFactoryV {

    private ResponseFactoryV() {
    }

    public static ControlListResponse buildControlListResponse(UserInfoRequest request, ControlList controlList) {
        ControlListResponseData data = ControlListResponseData.builder().validationCode(controlList.getValidationCode())
                .validationName(controlList.getValidationName()).build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return ControlListResponse.builder().data(data).meta(metaResponse).build();
    }
}
