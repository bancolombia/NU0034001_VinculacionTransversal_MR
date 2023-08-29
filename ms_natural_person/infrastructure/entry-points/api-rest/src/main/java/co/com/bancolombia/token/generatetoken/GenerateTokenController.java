package co.com.bancolombia.token.generatetoken;

import co.com.bancolombia.NaturalPersonController;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.validations.ValidationAcquisitionId;
import co.com.bancolombia.commonsvnt.api.validations.ValidationMandatory;
import co.com.bancolombia.genericstep.GenericStep;
import co.com.bancolombia.logfunctionalvnt.log.annotation.ILogRegister;
import co.com.bancolombia.model.generatetoken.GenerateToken;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequest;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenRequestData;
import co.com.bancolombia.model.token.generatetoken.GenerateTokenResponse;
import co.com.bancolombia.token.ResponseFactoryToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

import java.text.ParseException;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.API_CUSTOMER_VALUE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GENERATE_TOKEN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.META;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@NaturalPersonController
@Api(tags = {"AcquisitionInformation",})
public class GenerateTokenController implements GenerateTokenOperations {

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private GenerateTokenStartProcess generateTokenStartProcess;

    @Autowired
    private GenericStep genericStep;

    @ILogRegister(api = API_CUSTOMER_VALUE, operation = CODE_GENERATE_TOKEN)
    public ResponseEntity<GenerateTokenResponse> generateToken(
            @ApiParam(value = "Information related to generate token", required = true)
            @Validated({ValidationMandatory.class, ValidationAcquisitionId.class})
            @RequestBody GenerateTokenRequest body) throws ParseException {

        GenerateTokenRequestData data = body.getData();
        MetaRequest meta = body.getMeta();

        webRequest.setAttribute(META, meta, SCOPE_REQUEST);

        genericStep.firstStepForLogFunctional(data, meta, CODE_GENERATE_TOKEN);
        genericStep.validRequest(data);

        GenerateToken generateToken = generateTokenStartProcess.startProcess(data, meta);

        genericStep.finallyStep(data.getAcquisitionId(), generateToken.getInfoReuseCommon(), CODE_GENERATE_TOKEN);

        return new ResponseEntity<>(ResponseFactoryToken.buildGenerateTokenResponse(body, generateToken),
                HttpStatus.OK);
    }
}
