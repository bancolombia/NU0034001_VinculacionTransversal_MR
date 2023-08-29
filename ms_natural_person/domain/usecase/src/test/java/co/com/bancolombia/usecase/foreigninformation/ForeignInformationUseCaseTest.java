package co.com.bancolombia.usecase.foreigninformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.foreigninformation.ForeignInformation;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import co.com.bancolombia.model.foreigninformation.gateways.ForeignInformationRepository;
import co.com.bancolombia.model.foreigninformationcurrency.ForeignInformationCurrency;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.usecase.foreigninformationcurrency.ForeignInformationCurrencyUseCaseImpl;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ForeignInformationUseCaseTest {

    @InjectMocks
    @Spy
    ForeignInformationUseCaseImpl useCase;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;
    @Mock
    ValidateCatalogsForeignUseCase validateCatalogsForeignUseCase;
    @Mock
    MergeUseCase mergeUseCase;
    @Mock
    ForeignInformationCurrencyUseCaseImpl currencyUseCase;
    @Mock
    ForeignInformationRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void find(){
        ForeignInformation foreignInformation = ForeignInformation.builder().build();
        doReturn(foreignInformation).when(repository).findByAcquisition(any(Acquisition.class));
        ForeignInformation foreignInformation1 = useCase.
                findByAcquisition(Acquisition.builder().build());
        assertNotNull(foreignInformation1);
    }

    @Test
    public void startProcessForeignInformationTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ForeignInformationCurrency> list = Collections.singletonList(ForeignInformationCurrency.builder().build());
        ForeignInformation fI = ForeignInformation.builder().foreignCurrencyTransaction("").acquisition(acquisition)
                .build();
        doNothing().when(validateCatalogsForeignUseCase).validateForeignInformation(any(ForeignInformation.class));
        doNothing().when(validateCatalogsForeignUseCase).validateForeignIInformationCurrency(anyList(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doReturn(null).when(repository).findByAcquisition(any(Acquisition.class));
        ChecklistReply checklistReply = ChecklistReply.builder()
                .execFieldList(Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),
                anyString());
        doNothing().when(useCase).mandatoryForeignInformation(any(ForeignInformation.class), anyList());
        doReturn(new ArrayList<>()).when(currencyUseCase)
                .mandatoryMergeForeignInfo(any(ForeignInformation.class), anyList(), anyList(), anyBoolean());
        doReturn(ForeignInformationOperation.builder().foreignInformation(fI).build()).when(useCase)
                .save(any(ForeignInformation.class), anyList());

        ForeignInformationOperation foreignInformationOperation = useCase.startProcessForeignInformation(
                ForeignInformationOperation.builder().foreignInformation(fI).list(list).build());
        assertNotNull(foreignInformationOperation);
    }

    @Test
    public void startProcessForeignInformationMergeTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ForeignInformation fI = ForeignInformation.builder().foreignCurrencyTransaction("").acquisition(acquisition)
                .build();
        List<ForeignInformationCurrency> list = new ArrayList<>();
        doReturn(new ArrayList<>()).when(currencyUseCase).validateListNull(
                ForeignInformationOperation.builder().foreignInformation(fI).list(new ArrayList<>()).build(), fI);
        doNothing().when(validateCatalogsForeignUseCase).validateForeignInformation(any(ForeignInformation.class));
        doNothing().when(validateCatalogsForeignUseCase).validateForeignIInformationCurrency(anyList(), anyString());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        doReturn(fI).when(repository).findByAcquisition(any(Acquisition.class));
        ChecklistReply checklistReply = ChecklistReply.builder()
                .execFieldList(Collections.singletonList(ExecFieldReply.builder().mandatory(true).build())).build();
        doReturn(checklistReply).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class),
                anyString());
        doNothing().when(useCase).mandatoryForeignInformation(any(ForeignInformation.class), anyList());
        doReturn(new ArrayList<>()).when(currencyUseCase)
                .mandatoryMergeForeignInfo(any(ForeignInformation.class), anyList(), anyList(), anyBoolean());
        doReturn(ForeignInformationOperation.builder().foreignInformation(fI).build()).when(useCase)
                .save(any(ForeignInformation.class), anyList());

        ForeignInformationOperation foreignInformationOperation = useCase.startProcessForeignInformation(
                ForeignInformationOperation.builder().foreignInformation(fI).list(new ArrayList<>()).build());
        assertNotNull(foreignInformationOperation);
    }

    @Test
    public void mergeForeignInformationTest() {
        List<ErrorField> errorFields = new ArrayList<>();
        doReturn(errorFields).when(mergeUseCase).merge(any(ForeignInformation.class),
                any(ForeignInformation.class), any(MergeAttrib.class));
        this.useCase.mergeForeignInformation(ForeignInformation.builder().build(),
                ForeignInformation.builder().build());
        verify(this.useCase, times(1)).mergeForeignInformation(ForeignInformation.builder().build(),
                ForeignInformation.builder().build());
    }

    @Test(expected = ValidationException.class)
    public void mergeForeignInformationExceptionTest() {
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        doReturn(errorFields).when(mergeUseCase).merge(any(ForeignInformation.class),
                any(ForeignInformation.class), any(MergeAttrib.class));
        this.useCase.mergeForeignInformation(ForeignInformation.builder().build(),
                ForeignInformation.builder().build());
        verify(this.useCase, times(1)).mergeForeignInformation(ForeignInformation.builder().build(),
                ForeignInformation.builder().build());
    }

    @Test
    public void mandatoryForeignInformationTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ErrorField> errorFields = new ArrayList<>();
        List<ExecFieldReply> execFields = new ArrayList<>();
        doReturn(errorFields).when(useCase).validateMandatory(any(ForeignInformation.class), anyList(),
                anyString(), any(DependentFieldParamValidator.class));
        this.useCase.mandatoryForeignInformation(
                ForeignInformation.builder().acquisition(acquisition).foreignCurrencyTransaction("S").build(),
                execFields);
        verify(this.useCase, times(1)).mandatoryForeignInformation(
                ForeignInformation.builder().acquisition(acquisition).foreignCurrencyTransaction("S").build(),
                execFields);
    }

    @Test
    public void mandatoryForeignInformationNullTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ExecFieldReply> execFields = new ArrayList<>();
        doReturn(null).when(useCase).validateMandatory(any(ForeignInformation.class), anyList(), anyString(),
                any(DependentFieldParamValidator.class));
        this.useCase.mandatoryForeignInformation(
                ForeignInformation.builder().acquisition(acquisition).foreignCurrencyTransaction("S").build(),
                execFields);
        verify(this.useCase, times(1)).mandatoryForeignInformation(
                ForeignInformation.builder().acquisition(acquisition).foreignCurrencyTransaction("S").build(),
                execFields);
    }

    @Test(expected = ValidationException.class)
    public void mandatoryForeignInformationExceptionTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());
        List<ExecFieldReply> execFields = new ArrayList<>();
        doReturn(errorFields).when(useCase).validateMandatory(any(ForeignInformation.class), anyList(),
                anyString(), any(DependentFieldParamValidator.class));
        this.useCase.mandatoryForeignInformation(
                ForeignInformation.builder().acquisition(acquisition).foreignCurrencyTransaction("S").build(),
                execFields);
        verify(this.useCase, times(1)).mandatoryForeignInformation(
                ForeignInformation.builder().acquisition(acquisition).foreignCurrencyTransaction("S").build(),
                execFields);
    }

    @Test
    public void saveTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ForeignInformation fiSaved = ForeignInformation.builder().acquisition(acquisition).build();
        doReturn(fiSaved).when(repository).save(any(ForeignInformation.class));
        doReturn(new ArrayList<>()).when(currencyUseCase).save(any(ForeignInformation.class), anyList());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());
        ForeignInformationOperation fo = this.useCase.save(fiSaved, new ArrayList<>());
        assertNotNull(fo);
    }
}