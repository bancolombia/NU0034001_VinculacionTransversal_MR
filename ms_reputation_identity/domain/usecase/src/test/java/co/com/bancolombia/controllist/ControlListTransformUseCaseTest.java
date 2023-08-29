package co.com.bancolombia.controllist;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.InfoReuseCommon;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.model.controllist.ControlList;
import co.com.bancolombia.model.controllist.ControlListResponse;
import co.com.bancolombia.model.controllist.DataControlList;
import co.com.bancolombia.model.controllist.MetaControlList;
import co.com.bancolombia.model.controllist.StatusControlList;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
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

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CTR_LIST_VIGENTE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;

@RequiredArgsConstructor
public class ControlListTransformUseCaseTest {

    @InjectMocks
    @Spy
    private ControlListTransformUseCaseImpl transformUseCase;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transformInfoControlListTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("")
                .alerts("6").message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        ControlList controlList = transformUseCase.transformInfoControlList(acquisitionReply, response);
        assertNotNull(controlList);
    }

    @Test
    public void transformInfoControlListNotTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("")
                .alerts("3").message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        Mockito.doReturn(null).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        ControlList controlList = transformUseCase.transformInfoControlList(acquisitionReply, response);
        assertNotNull(controlList);
    }

    @Test
    public void transformInfoControlListNullTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state("").passport("")
                .alerts("12").message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        ControlList controlList = transformUseCase.transformInfoControlList(acquisitionReply, response);
        assertNull(controlList);
    }

    @Test(expected = ValidationException.class)
    public void transformInfoControlListExceptionTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state(CTR_LIST_VIGENTE).passport("")
                .alerts("3").message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        Mockito.doReturn(null).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        transformUseCase.transformInfoControlList(acquisitionReply, response);
    }

    @Test(expected = ValidationException.class)
    public void transformInfoControlListExceptionTwoTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().documentTypeHomologous("")
                .documentNumber("").acquisitionId("").build();
        StatusControlList status = StatusControlList.builder().code("200").build();
        InfoReuseCommon infoReuseCommon = InfoReuseCommon.builder().responseReuse("").requestReuse("")
                .dateResponseReuse(new Date()).dateResponseReuse(new Date()).build();
        DataControlList dataControlList = DataControlList.builder().status(status).customerDocumentType("")
                .customerDocumentId("").thirdPartyControl("").customerStatus("").state(CTR_LIST_VIGENTE).passport("")
                .alerts("2").message("").categories("").ofacMessageOther("").build();
        MetaControlList meta = MetaControlList.builder().messageId("").build();
        ControlListResponse response = ControlListResponse.builder().data(Collections.singletonList(dataControlList))
                .infoReuseCommon(infoReuseCommon).meta(meta).build();
        Mockito.doNothing().when(vinculationUpdateUseCase).markOperation(anyString(), anyString(), anyString());
        Mockito.doReturn(null).when(vinculationUpdateUseCase).updateAcquisition(anyString(), anyString());
        transformUseCase.transformInfoControlList(acquisitionReply, response);
    }
}
