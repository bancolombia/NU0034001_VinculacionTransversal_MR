package co.com.bancolombia.usecase.generatetoken;

import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Constants;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.generatetoken.reuserequest.GTAccountInformation;
import co.com.bancolombia.model.generatetoken.reuserequest.GTCellphoneInformation;
import co.com.bancolombia.model.generatetoken.reuserequest.GTCustomerIdentification;
import co.com.bancolombia.model.generatetoken.reuserequest.GTEmailInformation;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequest;
import co.com.bancolombia.model.generatetoken.reuserequest.GTRequestItem;
import co.com.bancolombia.model.generatetoken.reuserequest.GTTechTokenInfo;
import co.com.bancolombia.model.generatetoken.reuserequest.GTTechincalTokenInformation;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponse;
import co.com.bancolombia.model.generatetoken.reuseresponse.GTResponseWithLog;
import lombok.RequiredArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ENCRYPTED_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SEND_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SERVER_ID_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SOURCE_SYSTEM_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TEMPLATE_CODE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.VALIDITY_TOKEN;

@RequiredArgsConstructor
public class GenerateTokenConstructUseCaseImpl implements GenerateTokenConstructUseCase {

    private final CoreFunctionDate coreFunctionDate;

    public GTRequest createRequestGetToken(
            GenerateToken generateToken, String documentNumber, String documentType){
        GTTechTokenInfo techTokenInfo = GTTechTokenInfo.builder()
                .generateEncryptedToken(ENCRYPTED_TOKEN).tokenValidity(VALIDITY_TOKEN).build();

        GTTechincalTokenInformation technicalTokenInformation = GTTechincalTokenInformation.builder()
                .sendToken(SEND_TOKEN)
                .serverId(SERVER_ID_TOKEN)
                .sourceSystemId(SOURCE_SYSTEM_ID)
                .techTokenInfo(techTokenInfo).build();

        GTCustomerIdentification customerIdentification = GTCustomerIdentification.builder()
                .documentNumber(documentNumber).documentType(documentType).build();

        GTAccountInformation accountInformation = GTAccountInformation.builder()
                .accountNumber(Constants.ACCOUNT_NUMBER).accountType(Constants.ACCOUNT_TYPE).build();

        GTCellphoneInformation cellphoneInformation = GTCellphoneInformation.builder()
                .cellphoneNumber(generateToken.getCellphone()).build();

        GTEmailInformation emailInformation = GTEmailInformation.builder()
                .email(generateToken.getEmail()).alertTemplateCode(TEMPLATE_CODE).build();

        GTRequestItem requestItem = GTRequestItem.builder()
                .techincalTokenInformation(technicalTokenInformation)
                .customerIdentification(customerIdentification)
                .accountInformation(accountInformation)
                .cellphoneInformation(cellphoneInformation)
                .emailInformation(emailInformation).build();
        return GTRequest.builder().data(requestItem).build();
    }

    public GenerateToken reFormatGenerateToken(
            GenerateToken generateToken, BasicAcquisitionRequest request,
            GTResponseWithLog generateTokenResponseWithLog) throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GTResponse response = generateTokenResponseWithLog.getGenerateTokenResponse()
                .getData().getGenerateTokenResponse();
        String dateFinal = response.getResponseDate().toLowerCase().replaceAll("[t-z]", " ").trim();
        Date now = coreFunctionDate.getDatetime();
        return generateToken.toBuilder()
                .answerCode(response.getAnswerCode())
                .answerName(response.getAnswerDescription())
                .messageId(request.getMessageId())
                .requestDate(now).createdDate(now)
                .responseDate(df.parse(dateFinal))
                .infoReuseCommon(generateTokenResponseWithLog.getInfoReuseCommon())
                .build();
    }
}
