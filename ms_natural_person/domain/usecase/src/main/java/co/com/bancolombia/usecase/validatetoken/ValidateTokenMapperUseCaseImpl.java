package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.validatetoken.Meta;
import co.com.bancolombia.model.validatetoken.Response;
import co.com.bancolombia.model.validatetoken.ValidateToken;
import co.com.bancolombia.model.validatetoken.ValidateTokenResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateTokenMapperUseCaseImpl implements ValidateTokenMapperUseCase {

    private final CoreFunctionDate coreFunctionDate;

    @Override
    public ValidateToken mapperFromValidateToken(
            ValidateToken validateToken, ValidateTokenResponse validateTokenResponse,
            BasicAcquisitionRequest request, String cellphone) {
        Response data = validateTokenResponse.getData().getGenerateTokenResponse();
        Meta meta = validateTokenResponse.getMeta();
        return validateToken.toBuilder()
                .customerDocumentType(request.getDocumentType())
                .customerDocumentNumber(request.getDocumentNumber()).answerCode(data.getAnswerCode())
                .answerDescription(data.getAnswerDescription()).cellphoneNumber(cellphone)
                .messageId(meta.getMessageId()).requestDate(meta.getRequestDate())
                .responseDate(data.getResponseDate()).createdDate(coreFunctionDate.getDatetime())
                .createdBy(request.getUserTransaction()).build();
    }
}
