package co.com.bancolombia.api.clausesinstructions;

import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.api.clauseinstructions.ClauseInstructionsController;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.model.containeraction.ContainerAction;
import co.com.bancolombia.commonsvnt.model.matrixacquisition.MatrixAcquisition;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixInfoClause;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.instruction.InstructionClausesUseCase;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import co.com.bancolombia.model.acquisition.ClauseInstructions;
import co.com.bancolombia.model.acquisition.ClauseInstructionsWithAcquisition;
import co.com.bancolombia.model.instruction.Instruction;
import co.com.bancolombia.model.matrixinstruction.MatrixInstruction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ConsultInstructionsTest {

    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonUserInfoRequest;

    @InjectMocks
    @Spy
    private ClauseInstructionsController clauseInstructionsController;

    @Mock
    private InstructionClausesUseCase instructionUseCase;

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

    private String documentType;
    private String documentNumber;
    private String acquisitionId;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(clauseInstructionsController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        acquisitionId = "df3b8c71-2911-4977-a66f-d6c2e23e3310";
        documentType = "TIPDOC_FS001";
        documentNumber = "1061000000";
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(clauseInstructionsController);
    }

    @Test
    public void validConsultInstructionsTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        Clause clause = Clause.builder().code("0001").name("TEST CLAUSE").build();
        Instruction instruction = Instruction.builder()
                .code("INST1")
                .name("Instruction 1").build();

        Step step = Step.builder().code("STP1").name("STEP 1").build();

        MatrixAcquisition mAcquisition = MatrixAcquisition.builder().step(step).build();

        List<MatrixInstruction> mInstructions = Stream.of(MatrixInstruction.builder()
                .instruction(instruction)
                .matrixAcquisition(mAcquisition)
                .sequence(1)
                .build()).collect(Collectors.toList());

        List<MatrixInfoClause> matrixInfoClauses = Stream.of(MatrixInfoClause.builder()
                .clause(clause)
                .step(step)
                .containerActions(Collections.singletonList(ContainerAction.builder().code("").build()))
                .typeAcquisition(TypeAcquisition.builder().code("TIPDOC_FS001").build())
                .sequence(1)
                .build()).collect(Collectors.toList());

        ClauseInstructions ret = ClauseInstructions.builder()
                .clauses(matrixInfoClauses)
                .instructions(mInstructions).build();

        ClauseInstructionsWithAcquisition clauseInstructionsWithAcquisition =
                ClauseInstructionsWithAcquisition.builder().clauseInstructions(ret).build();

        when(instructionUseCase.searchClausesAndInstructions(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(clauseInstructionsWithAcquisition);

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    public void nullConsultInstructionsTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        when(instructionUseCase.searchClausesAndInstructions(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(null);

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void invalidDocNumDocTypeTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, "FS001", documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocNumTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, null);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocTypeTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, null, documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void emptyDocNumTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, "");

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void emptyDocTypeTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, "", documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void repeatedErrorTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("consult-instructions");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId,documentType, "106100000010610000001");

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonUserInfoRequest.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/consult-instructions")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
