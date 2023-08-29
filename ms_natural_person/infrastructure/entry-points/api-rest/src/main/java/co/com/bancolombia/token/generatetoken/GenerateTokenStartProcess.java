package co.com.bancolombia.token.generatetoken;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.commonsvnt.model.documenttype.DocumentType;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequestData;
import co.com.bancolombia.usecase.generatetoken.GenerateTokenUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GENERATE_TOKEN;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class GenerateTokenStartProcess {

    @Autowired
    private CoreFunctionString coreFunctionString;

    @Autowired
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Autowired
    private GenerateTokenUseCase generateTokenUseCase;

    public GenerateToken startProcess(GenerateTokenRequestData data, MetaRequest meta) throws ParseException {
        AcquisitionReply acquisitionReply = vinculationUpdateUseCase.validateAcquisition(
                data.getAcquisitionId(), data.getDocumentType(),
                data.getDocumentNumber(), CODE_GENERATE_TOKEN);

        Acquisition acquisition = Acquisition.builder()
                .id(UUID.fromString(acquisitionReply.getAcquisitionId()))
                .documentNumber(acquisitionReply.getDocumentNumber())
                .typeAcquisition(TypeAcquisition.builder().code(acquisitionReply.getCodeTypeAcquisition()).build())
                .documentType(DocumentType.builder().code(acquisitionReply.getDocumentTypeCodeGenericType()).build())
                .build();

        BasicAcquisitionRequest ba = BasicAcquisitionRequest.builder()
                .idAcq(data.getAcquisitionId()).documentType(data.getDocumentType())
                .documentNumber(data.getDocumentNumber()).userTransaction(meta.getUsrMod())
                .messageId(meta.getMessageId()).build();

        GenerateToken generateToken = GenerateToken.builder()
                .email(coreFunctionString.lowerCaseString(data.getEmail())).cellphone(data.getCellphone())
                .acquisition(acquisition).createdBy(meta.getUsrMod()).build();
        return generateTokenUseCase.startProcessGenerateToken(ba, generateToken);
    }
}
