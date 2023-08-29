package co.com.bancolombia.economicinformation;

import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.economicinformation.model.EconomicInfoRequest;
import co.com.bancolombia.economicinformation.model.EconomicInfoResponse;
import co.com.bancolombia.economicinformation.model.EconomicInfoResponseData;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ECONOMIC_NO_REQUIRE_RUT_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ECONOMIC_NO_REQUIRE_RUT_NAME;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ECONOMIC_REQUIRE_RUT_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ECONOMIC_REQUIRE_RUT_NAME;

public class ResponseFactoryEconomic {
    public static EconomicInfoResponse buildRequiredRutEconomicResponse(EconomicInfoRequest request) {
        EconomicInfoResponseData data = EconomicInfoResponseData
                .builder()
                .answerCode(ECONOMIC_REQUIRE_RUT_CODE)
                .answerName(ECONOMIC_REQUIRE_RUT_NAME).build();
        return EconomicInfoResponse.builder().data(data).meta(MetaResponse.fromMeta(request.getMeta())).build();
    }

    public static EconomicInfoResponse buildNoRequiredRutEconomicResponse(EconomicInfoRequest request) {
        EconomicInfoResponseData data = EconomicInfoResponseData
                .builder()
                .answerCode(ECONOMIC_NO_REQUIRE_RUT_CODE)
                .answerName(ECONOMIC_NO_REQUIRE_RUT_NAME).build();
        return EconomicInfoResponse.builder().data(data).meta(MetaResponse.fromMeta(request.getMeta())).build();
    }
}
