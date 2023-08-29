package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.rabbit.naturalperson.reply.ValidateIdentityReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.AcquisitionReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.rabbit.NaturalPersonUseCase;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ExpoQuestionValidationUseCaseTest {

    @InjectMocks
    @Spy
    private ExpoQuestionValidationUseCaseImpl expoQuestionVUC;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private NaturalPersonUseCase naturalPersonUseCase;

    @Mock
    private CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validationStageOneCodeFourTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        ChecklistReply checklistCodeFour = ChecklistReply.builder().stateOperation("4").build();
        doReturn(checklistCodeFour).when(vinculationUpdateUseCase).checkListStatus(anyString(), anyString());
        expoQuestionVUC.validationStageOne(acquisitionReply);
        Mockito.verify(this.expoQuestionVUC, Mockito.times(1))
                .validationStageOne(acquisitionReply);
    }

    @Test(expected = ValidationException.class)
    public void validationStageOneCodeOneTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        ChecklistReply checklistCodeOne = ChecklistReply.builder().stateOperation("1").build();
        doReturn(checklistCodeOne).when(vinculationUpdateUseCase).checkListStatus(anyString(), anyString());
        expoQuestionVUC.validationStageOne(acquisitionReply);
        Mockito.verify(this.expoQuestionVUC, Mockito.times(1))
                .validationStageOne(acquisitionReply);
    }

    @Test
    public void validateMissingDataTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        doReturn(ValidateIdentityReply.builder().firstName("").firstSurname("").expeditionDate(new Date()).build())
                .when(naturalPersonUseCase).validateIdentity(anyString());
        expoQuestionVUC.validateMissingData(acquisitionReply);
    }

    @Test(expected = ValidationException.class)
    public void validateMissingDataNullTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        doReturn(ValidateIdentityReply.builder().firstName(null).firstSurname(null).expeditionDate(null).build())
                .when(naturalPersonUseCase).validateIdentity(anyString());
        expoQuestionVUC.validateMissingData(acquisitionReply);
    }

    @Test(expected = ValidationException.class)
    public void validateMissingDataNotFirstNameTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentNumber("1234")
                .documentTypeCodeGenericType("FS001").build();
        doReturn(ValidateIdentityReply.builder().firstSurname("").expeditionDate(new Date()).build())
                .when(naturalPersonUseCase).validateIdentity(anyString());
        expoQuestionVUC.validateMissingData(acquisitionReply);
    }

    @Test(expected = ValidationException.class)
    public void validateExceptionTest() {
        expoQuestionVUC.validateException("", "", "", "");
        Mockito.verify(this.expoQuestionVUC, Mockito.times(1)).validateException("", "", "", "");
    }

    @Test
    public void blockCustomerTest() {
        AcquisitionReply acquisitionReply = AcquisitionReply.builder().acquisitionId("").documentType("TIPDOC_FS001")
                .documentNumber("12345").build();
        String code = "1";
        String dateBlock = "1 dia";
        Date date = new Date();
        doReturn(date).when(coreFD).timeUnBlockCustomer(anyString());
        Mockito.doNothing().when(vinculationUpdateUseCase).blockCustomer(anyString(), anyString(), any(Date.class),
                anyString());
        doReturn(dateBlock).when(coreFD).compareDifferenceTime(any(Date.class), anyString(),
                anyBoolean(), anyBoolean());
        expoQuestionVUC.blockCustomer(acquisitionReply, code);
        Mockito.verify(expoQuestionVUC, Mockito.times(1)).blockCustomer(acquisitionReply, code);
    }
}