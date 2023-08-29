package co.com.bancolombia.controllist;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.query.ControlListSaveQuery;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.ControlListSaveReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.commons.BasicAcquisitionRequest;
import co.com.bancolombia.model.controllist.ControlList;
import co.com.bancolombia.model.controllist.ControlListRequest;
import co.com.bancolombia.model.controllist.ControlListResponse;
import co.com.bancolombia.model.controllist.ControlListSave;
import co.com.bancolombia.model.controllist.DataControlList;
import co.com.bancolombia.model.controllist.MetaControlList;
import co.com.bancolombia.model.controllist.StatusControlList;
import co.com.bancolombia.model.controllist.gateways.ControlListRestRepository;
import co.com.bancolombia.model.controllist.gateways.ControlListSaveRepository;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ControlListUseCaseTest {

    @InjectMocks
    @Spy
    private ControlListUseCaseImpl controlListUseCase;

    @Mock
    private ControlListRestRepository controlListRestRepository;

    @Mock
    private NaturalPersonUseCase naturalUseCase;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Mock
    private ControlListSaveRepository controlSaveRepository;

    @Mock
    private ControlListTransformUseCase controlListTransform;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startProcessControlListTest() {
        ValidateIdentityReply identityReply = ValidateIdentityReply.builder().acquisitionId("").firstName("")
                .firstSurname("").build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("").alerts("")
                .message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doReturn(identityReply).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(controlListRestRepository).getUserInfoFromControlList(any(ControlListRequest.class),
                anyString(), any(Date.class));
        Mockito.doReturn(ControlListSave.builder().build()).when(controlSaveRepository).save(any(ControlListSave.class));
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ControlList.builder().build()).when(controlListTransform).transformInfoControlList(
                any(AcquisitionReply.class), any(ControlListResponse.class));
        ControlList controlList = controlListUseCase.startProcessControlList(acquisitionReply, BasicAcquisitionRequest
                .builder().messageId("").userTransaction("").build());
        assertNotNull(controlList);
    }

    @Test
    public void startProcessControlListNotFoundTest() {
        ValidateIdentityReply identityReply = ValidateIdentityReply.builder().acquisitionId("").firstName("")
                .firstSurname("").build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("404").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("").alerts("")
                .message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doReturn(identityReply).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(controlListRestRepository).getUserInfoFromControlList(any(ControlListRequest.class),
                anyString(), any(Date.class));
        Mockito.doReturn(ControlListSave.builder().build()).when(controlSaveRepository).save(any(ControlListSave.class));
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ControlList.builder().build()).when(controlListTransform).transformInfoControlList(
                any(AcquisitionReply.class), any(ControlListResponse.class));
        ControlList controlList = controlListUseCase.startProcessControlList(acquisitionReply, BasicAcquisitionRequest
                .builder().messageId("").userTransaction("").build());
        assertNotNull(controlList);
    }

    @Test(expected = ValidationException.class)
    public void startProcessControlListExceptionTest() {
        ValidateIdentityReply identityReply = ValidateIdentityReply.builder().acquisitionId("").firstName("").build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("").alerts("")
                .message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doReturn(identityReply).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(controlListRestRepository).getUserInfoFromControlList(any(ControlListRequest.class),
                anyString(), any(Date.class));
        Mockito.doReturn(ControlListSave.builder().build()).when(controlSaveRepository).save(any(ControlListSave.class));
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ControlList.builder().build()).when(controlListTransform).transformInfoControlList(
                any(AcquisitionReply.class), any(ControlListResponse.class));
        ControlList controlList = controlListUseCase.startProcessControlList(acquisitionReply, BasicAcquisitionRequest
                .builder().messageId("").userTransaction("").build());
        assertNotNull(controlList);
    }

    @Test(expected = ValidationException.class)
    public void startProcessControlListExceptionTwoTest() {
        ValidateIdentityReply identityReply = ValidateIdentityReply.builder().acquisitionId("").firstSurname("").build();
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("").alerts("")
                .message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doReturn(identityReply).when(naturalUseCase).validateIdentity(anyString());
        Mockito.doReturn(response).when(controlListRestRepository).getUserInfoFromControlList(any(ControlListRequest.class),
                anyString(), any(Date.class));
        Mockito.doReturn(ControlListSave.builder().build()).when(controlSaveRepository).save(any(ControlListSave.class));
        Mockito.doReturn(new Date()).when(coreFunctionDate).getDatetime();
        Mockito.doReturn(ControlList.builder().build()).when(controlListTransform).transformInfoControlList(
                any(AcquisitionReply.class), any(ControlListResponse.class));
        ControlList controlList = controlListUseCase.startProcessControlList(acquisitionReply, BasicAcquisitionRequest
                .builder().messageId("").userTransaction("").build());
        assertNotNull(controlList);
    }

    @Test
    public void findStateValidationCustomerControlListTest() {
        doReturn("6").when(controlSaveRepository).findStateValidationCustomerControlList(anyString(), anyString(), anyString());
        String state = controlListUseCase.findStateValidationCustomerControlList("0009999999", "CC", "01233456");
        assertNotNull(state);
    }
}
