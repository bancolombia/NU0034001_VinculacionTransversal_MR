package co.com.bancolombia.api.clausesinstructions;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.clauseinstructions.ClauseInstructionsController;
import co.com.bancolombia.api.model.acceptclauses.AcceptClausesRequest;
import co.com.bancolombia.api.model.acceptclauses.AcceptClausesRequestData;
import co.com.bancolombia.clauseacquisitionchecklist.ClauseAcquisitionChecklistUseCase;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.model.acquisition.BasicAcquisitionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class AcceptClauseTest {

    private MockMvc mvc;

    private JacksonTester<AcceptClausesRequest> jsonAcceptClausesRequest;

    @InjectMocks
    @Spy
    private ClauseInstructionsController clauseInstructionsController;

    @Mock
    private ClauseAcquisitionChecklistUseCase clauseAcquisitionUseCase;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

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
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(clauseInstructionsController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(clauseInstructionsController);
    }

    @Test
    public void validAcceptClausesTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("accept-clauses");
        AcceptClausesRequestData acceptClausesRequestData = AcceptClausesRequestData.builder()
                .acquisitionId("df3b8c71-2911-4977-a66f-d6c2e23e3310")
                .documentType("TIPDOC_FS001")
                .documentNumber("1061000000")
                .acceptClauses("1")
                .clauseCode("CLAUSE001").build();
        
        AcceptClausesRequest acceptClausesRequest = new AcceptClausesRequest(meta, acceptClausesRequestData);

        String json = jsonAcceptClausesRequest.write(acceptClausesRequest).getJson();

        Acquisition acquisition = Acquisition.builder().build();
        
        doReturn(acquisition).when(clauseAcquisitionUseCase)
                   .acceptClause(Mockito.any(BasicAcquisitionRequest.class), Mockito.any(String.class),
                                 Mockito.any(String.class), Mockito.anyString());

        MockHttpServletResponse response = mvc.perform(
            post("/acquisition-update/api/v1/accept-clauses")
                    .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
