package co.com.bancolombia.api.acquisition;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.api.TestUtils;
import co.com.bancolombia.checklist.CheckListUseCase;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequestData;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.activity.Activity;
import co.com.bancolombia.commonsvnt.model.clause.Clause;
import co.com.bancolombia.commonsvnt.model.clauseacquisitionchecklist.ClauseAcquisitionCheckList;
import co.com.bancolombia.commonsvnt.model.containeraction.ContainerAction;
import co.com.bancolombia.commonsvnt.model.matrixtypeacquisitionclause.MatrixTypeAcquisitionClause;
import co.com.bancolombia.commonsvnt.model.startlist.StartList;
import co.com.bancolombia.commonsvnt.model.step.Step;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.logfunctionalvnt.log.api.exceptionhandling.GlobalExceptionHandler;
import co.com.bancolombia.logfunctionalvnt.log.usescases.acquisition.ILogFuncAcquisitionUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.checklist.ILogFuncCheckListUseCase;
import co.com.bancolombia.logfunctionalvnt.log.usescases.field.ILogFuncFieldUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class ValidateStatusTest {

    private MockMvc mvc;

    private JacksonTester<UserInfoRequest> jsonValidateStatus;

    @InjectMocks
    @Spy
    private ValidateStatusController acquisitionController;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private CheckListUseCase checkListUseCase;

    @Mock
    private AcquisitionUseCase acquisitionUseCase;

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
        mvc = MockMvcBuilders.standaloneSetup(acquisitionController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();

        acquisitionId = "df3b8c71-2911-4977-a66f-d6c2e23e3310";
        documentType = "TIPDOC_FS001";
        documentNumber = "1061000000";
    }

    @Test
    @SneakyThrows(Exception.class)
    public void contextLoads() {
        assertNotNull(acquisitionController);
        assertNotNull(checkListUseCase);
    }

    @Test
    @SneakyThrows(Exception.class)
    public void validValidateStatusTest() {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, documentNumber);

        UserInfoRequest request = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(request).getJson();

        Activity activity = Activity.builder()
                .idActivity("001").nameActivity("Activity 1").codeStateActivity("1").nameStateActivity("Name State 1")
                .build();

        MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .id(UUID.randomUUID()).active(true).sequence(1)
                .clause(Clause.builder().id(UUID.randomUUID()).code("001").name("Clause 001").build())
                .action(ContainerAction.builder().id(UUID.randomUUID()).code("001").name("Container 001").build())
                .step(Step.builder()
                        .id(UUID.randomUUID()).code("1").name("Step 1").active(true).operation("Operation").build())
                .typeAcquisition(TypeAcquisition.builder().build())
                .build();

        ClauseAcquisitionCheckList checkList = ClauseAcquisitionCheckList.builder()
                .id(UUID.randomUUID()).acceptClause(true).dateAcceptClause(new Date())
                .acquisition(Acquisition.builder().id(UUID.fromString(acquisitionId)).build())
                .matrixTypeAcquisitionClause(matrixTypeAcquisitionClause)
                .build();

        List<Activity> activities = Collections.singletonList(activity);
        List<ClauseAcquisitionCheckList> checkLists = Collections.singletonList(checkList);

        StartList startList = StartList.builder()
                .activitiesList(activities)
                .acquisitionId(acquisitionId).documentType(documentType).documentNumber(documentNumber)
                .registrationDate(new SimpleDateFormat(DATE_FORMAT_TIME).format(new Date()))
                .codeStateAcquisition("1").nameStateAcquisition("Name State 1")
                .clauseAcquisitionCheckLists(checkLists).build();

        List<StartList> startLists = Collections.singletonList(startList);

        when(checkListUseCase.getProcessesCheckList(any(UUID.class), anyString(), anyString(), anyString()))
                .thenReturn(startLists);

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows(Exception.class)
    public void validValidateStatusNoAcceptClauseTest() {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, documentNumber);

        UserInfoRequest request = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(request).getJson();

        Activity activity = Activity.builder()
                .idActivity("001").nameActivity("Activity 1").codeStateActivity("1").nameStateActivity("Name State 1")
                .build();

        MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .id(UUID.randomUUID()).active(true).sequence(1)
                .clause(Clause.builder().id(UUID.randomUUID()).code("001").name("Clause 001").build())
                .action(ContainerAction.builder().id(UUID.randomUUID()).code("001").name("Container 001").build())
                .step(Step.builder()
                        .id(UUID.randomUUID()).code("1").name("Step 1").active(true).operation("Operation").build())
                .typeAcquisition(TypeAcquisition.builder().build())
                .build();

        ClauseAcquisitionCheckList checkList = ClauseAcquisitionCheckList.builder()
                .id(UUID.randomUUID()).acceptClause(false).dateAcceptClause(null)
                .acquisition(Acquisition.builder().id(UUID.fromString(acquisitionId)).build())
                .matrixTypeAcquisitionClause(matrixTypeAcquisitionClause)
                .build();

        List<Activity> activities = Collections.singletonList(activity);
        List<ClauseAcquisitionCheckList> checkLists = Collections.singletonList(checkList);

        StartList startList = StartList.builder()
                .activitiesList(activities)
                .acquisitionId(acquisitionId).documentType(documentType).documentNumber(documentNumber)
                .registrationDate(new SimpleDateFormat(DATE_FORMAT_TIME).format(new Date()))
                .codeStateAcquisition("1").nameStateAcquisition("Name State 1")
                .clauseAcquisitionCheckLists(checkLists).build();

        List<StartList> startLists = Collections.singletonList(startList);

        when(checkListUseCase.getProcessesCheckList(any(UUID.class), anyString(), anyString(), anyString()))
                .thenReturn(startLists);

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows(Exception.class)
    public void validValidateStatusFindAcquisitionTest() {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, documentNumber);

        UserInfoRequest request = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(request).getJson();

        Activity activity = Activity.builder()
                .idActivity("001").nameActivity("Activity 1").codeStateActivity("1").nameStateActivity("Name State 1")
                .build();

        MatrixTypeAcquisitionClause matrixTypeAcquisitionClause = MatrixTypeAcquisitionClause.builder()
                .id(UUID.randomUUID()).active(true).sequence(1)
                .clause(Clause.builder().id(UUID.randomUUID()).code("001").name("Clause 001").build())
                .action(ContainerAction.builder().id(UUID.randomUUID()).code("001").name("Container 001").build())
                .step(Step.builder()
                        .id(UUID.randomUUID()).code("1").name("Step 1").active(true).operation("Operation").build())
                .typeAcquisition(TypeAcquisition.builder().build())
                .build();

        ClauseAcquisitionCheckList checkList = ClauseAcquisitionCheckList.builder()
                .id(UUID.randomUUID()).acceptClause(false).dateAcceptClause(null)
                .acquisition(Acquisition.builder().id(UUID.fromString(acquisitionId)).build())
                .matrixTypeAcquisitionClause(matrixTypeAcquisitionClause)
                .build();

        List<Activity> activities = Collections.singletonList(activity);
        List<ClauseAcquisitionCheckList> checkLists = Collections.singletonList(checkList);

        StartList startList = StartList.builder()
                .activitiesList(activities)
                .acquisitionId(acquisitionId).documentType(documentType).documentNumber(documentNumber)
                .registrationDate(new SimpleDateFormat(DATE_FORMAT_TIME).format(new Date()))
                .codeStateAcquisition("1").nameStateAcquisition("Name State 1")
                .clauseAcquisitionCheckLists(checkLists).build();

        List<StartList> startLists = Collections.singletonList(startList);

        when(checkListUseCase.getProcessesCheckList(any(UUID.class), anyString(), anyString(), anyString()))
                .thenReturn(startLists);
        when(acquisitionUseCase.findById(any(UUID.class)))
                .thenReturn(Acquisition.builder().id(UUID.randomUUID()).build());

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows(Exception.class)
    public void validValidateStatusNullAcqIdTest() {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(null, documentType, documentNumber);

        UserInfoRequest request = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(request).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void invalidDocNumDocTypeTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, "FS001", documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocNumTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, documentType, null);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void nullDocTypeTest() throws Exception {
        MetaRequest meta = TestUtils.buildMetaRequest("validate-status");
        UserInfoRequestData data = new UserInfoRequestData(acquisitionId, null, documentNumber);

        UserInfoRequest userInfoRequest = new UserInfoRequest(meta, data);
        String json = jsonValidateStatus.write(userInfoRequest).getJson();

        MockHttpServletResponse response = mvc.perform(
                post("/acquisition-update/api/v1/validate-status")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}

