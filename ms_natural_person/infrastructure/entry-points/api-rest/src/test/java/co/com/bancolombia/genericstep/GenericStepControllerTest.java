package co.com.bancolombia.genericstep;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.MyDataInitial;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.model.acquisition.AcquisitionInitial;
import co.com.bancolombia.commonsvnt.model.basicinfo.BasicInfoRequestData;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.model.StepForLogClass;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_GEN_EXP_DOCS;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class GenericStepControllerTest {

    private MockMvc mvc;

    @InjectMocks
    @Spy
    private GenericStep genericStep;

    @Mock
    private WebRequest webRequest;
    @Mock
    private ILogFuncAcquisitionUseCase acquisitionUseCase;
    @Mock
    private ILogFuncFieldUseCase fieldUseCase;
    @Mock
    private ILogFuncCheckListUseCase checkListUseCase;
    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private StepForLogFunctional stepForLogFunctional;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(genericStep)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() {
        assertNotNull(genericStep);
    }

    @Test
    public void first(){
        MetaRequest meta = TestUtils.buildMetaRequest("gen-exp-documents");
        UserInfoRequestData data = UserInfoRequestData.builder().documentNumber("asd").documentType("asd").build();
        stepForLogFunctional.firstStepForLogFunctional(CODE_GEN_EXP_DOCS, AcquisitionInitial.builder()
                .meta(meta)
                .data(MyDataInitial.builder().documentNumber(data.getDocumentNumber())
                        .documentType(data.getDocumentType()).build()).build());
        StepForLogFunctional stepForLogFunctional1 = genericStep.firstStepForLogFunctional(
                data, meta, "asd");
        assertNotNull(stepForLogFunctional1);
    }

    @Test
    public void finallyStepTest(){
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder()
                .dateResponseReuse(new Date()).responseReuse("asd")
                .dateRequestReuse(new Date()).requestReuse("asd").mapFields(new HashMap<>()).build();
        doNothing().when(stepForLogFunctional).finallyStepForLogFunctional(any(StepForLogClass.class));
        genericStep.finallyStep(UUID.randomUUID().toString(), infoReuseCommon, "asd");
        assertNotNull(stepForLogFunctional);
    }

    @Test
    public void validRequest(){
        BasicInfoRequestData data = BasicInfoRequestData.builder().build();
        genericStep.validRequest(data);
        assertNotNull(data);
    }

}