package co.com.bancolombia.usecase.validatetoken;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.validatetoken.CellphoneInformation;
import co.com.bancolombia.model.validatetoken.CustomerIdentification;
import co.com.bancolombia.model.validatetoken.DataAuthentication;
import co.com.bancolombia.model.validatetoken.TokenInformation;
import co.com.bancolombia.model.validatetoken.ValidateTokenRequest;
import co.com.bancolombia.model.validatetoken.ValidateTokenRequestItem;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVER_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SOURCE_SYSTEM_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TOKEN_ENCRYPTION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TOKEN_VALIDITY;

@RequiredArgsConstructor
public class ValidateTokenCreateRequestUseCase {

    public ValidateTokenRequest createRequest(
            Acquisition acquisition, String documentNumber, String tokenCode, String cellphone){
        DataAuthentication dataAuthentication = DataAuthentication.builder()
                .serverId(SERVER_ID).sourceSystemId(SOURCE_SYSTEM_ID).build();

        CustomerIdentification customerIdentification = CustomerIdentification.builder()
                .documentType(acquisition.getDocumentType().getCodeHomologation())
                .documentNumber(documentNumber).build();

        CellphoneInformation cellphoneInformation = CellphoneInformation.builder().cellphoneNumber(cellphone).build();

        TokenInformation tokenInformation = TokenInformation.builder()
                .tokenEncryption(TOKEN_ENCRYPTION).inputToken(tokenCode)
                .tokenValidity(TOKEN_VALIDITY).build();

        ValidateTokenRequestItem validateTokenRequestItem = ValidateTokenRequestItem.builder()
                .dataAuthentication(dataAuthentication).customerIdentification(customerIdentification)
                .cellphoneInformation(cellphoneInformation).tokenInformation(tokenInformation).build();
        return ValidateTokenRequest.builder()
                .data(Stream.of(validateTokenRequestItem).collect(Collectors.toList())).build();
    }
}
