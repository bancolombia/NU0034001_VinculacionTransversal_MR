package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.logfunctionalvnt.log.util.StepForLogFunctional;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_START_ACQUISITION;
import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class GenericStepTest {

    @InjectMocks
    @Spy
    private GenericStep genericStep;

    @Mock
    private WebRequest webRequest;

    @Mock
    private ILogFuncAcquisitionUseCase iLogFuncAcquisitionUseCase;

    @Mock
    private ILogFuncFieldUseCase iLogFuncFieldUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ILogFuncCheckListUseCase iLogFuncCheckListUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void firstStepForLogFunctionalTest() {
        UserInfoRequestData data = UserInfoRequestData.builder()
                .acquisitionId(UUID.randomUUID().toString())
                .documentType("TIPDOC_FS001").documentNumber("1061000000").build();
        MetaRequest meta = TestUtils.buildMetaRequest("start-acquisition");

        StepForLogFunctional step = genericStep.firstStepForLogFunctional(data, meta, CODE_START_ACQUISITION);
        assertNotNull(step);
    }

    @Test
    public void finallyStepNotNullAcquisitionTest() {
        StepForLogFunctional step = StepForLogFunctional.builder()
                .webRequest(webRequest)
                .acquisitionUseCase(iLogFuncAcquisitionUseCase)
                .fieldUseCase(iLogFuncFieldUseCase)
                .coreFunctionDate(coreFunctionDate)
                .checkListUseCase(iLogFuncCheckListUseCase).build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();

        genericStep.finallyStep(step, UUID.randomUUID().toString(), info, CODE_START_ACQUISITION);
        assertNotNull(step);
    }

    @Test
    public void finallyStepNullAcquisitionTest() {
        StepForLogFunctional step = StepForLogFunctional.builder().build();
        InfoReuseCommon info = InfoReuseCommon.builder().build();

        genericStep.finallyStep(step, null, info, CODE_START_ACQUISITION);
        assertNotNull(step);
    }
}
