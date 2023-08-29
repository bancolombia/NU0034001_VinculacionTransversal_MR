package co.com.bancolombia.token;

import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequest;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenResponse;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenResponseData;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenResponse;
import co.com.bancolombia.model.token.validatetoken.ValidateTokenResponseData;
import co.com.bancolombia.model.validatetoken.ValidateToken;

public class ResponseFactoryToken {
    private ResponseFactoryToken() {
    }

    public static GenerateTokenResponse buildGenerateTokenResponse(
            GenerateTokenRequest request, GenerateToken generateToken){
        GenerateTokenResponseData data = GenerateTokenResponseData.builder()
                .answerCode(generateToken.getAnswerCode())
                .answerName(generateToken.getAnswerName())
                .build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return GenerateTokenResponse.builder().data(data).meta(metaResponse).build();
    }

    public static ValidateTokenResponse buildValidateTokenResponse(
            ValidateTokenRequest request, ValidateToken validateToken) {
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());

        ValidateTokenResponseData validTokenData = ValidateTokenResponseData.builder()
                .answerCode(validateToken.getAnswerCode())
                .answerName(validateToken.getAnswerDescription()).build();
        return ValidateTokenResponse.builder().meta(metaResponse).data(validTokenData).build();
    }
}