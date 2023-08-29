package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.usecase.contactinformation.generic.ArrayErrors;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ContactInformationProcessUseCaseTest {

    @InjectMocks
    @Spy
    private ContactInformationProcessUseCase contactInformationProcessUseCase;
    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;
    @Mock
    private ValidateCatalogsContactUseCase validateCatalogsContactUseCase;
    @Mock
    ContactIValidationUseCase contactIValidationUseCase;
    @Mock
    ContactInformationRepository contactInformationRepository;
    @Mock
    ContactInformationUseCasePersist cInfoUCPer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void concatErrorTypeDirectionTest() {
        String tpDirection = "direction";
        String result = contactInformationProcessUseCase.concatErrorTypeDirection(tpDirection);
        assertEquals(" [TYPE ADRESS: direction]", result);
    }

    @Test
    public void startProcessNewContactInformationTest() {

        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doReturn(EmptyReply.builder().build()).when(validateCatalogsContactUseCase).validateContactInfoCatalogs(anyList());
        doReturn(new ArrayList<>()).when(contactInformationProcessUseCase)
                .validateMandatory(any(ContactInformation.class), anyList(), anyString(),
                        any(DependentFieldParamValidator.class));

        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doNothing().when(contactIValidationUseCase).validateAddressType(anyList());
        doNothing().when(contactIValidationUseCase).validateForeignCountry(anyList());

        doReturn(contactInformationRepository).when(cInfoUCPer).getContactInformationRepository();
        doReturn(new ArrayList<>()).when(contactInformationRepository).findAllByAcquisition(any(Acquisition.class));
        doReturn(true).when(contactIValidationUseCase).existsTypeAddressResBrand(any(Acquisition.class)
                , anyList(), anyList());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        ArrayErrors arrayErrors = new ArrayErrors(new ArrayList<>());
        Acquisition acquisition =Acquisition.builder().id(UUID.randomUUID()).typeAcquisition(TypeAcquisition.builder()
                .id(UUID.randomUUID()).code("VT001").build()).build();
        List<ContactInformation> contactInformationList = contactInformationProcessUseCase.firstStepStartProcess
                (acquisition, Collections.singletonList(contactInformation), arrayErrors);
        assertNotNull(contactInformationList);
    }

    @Test
    public void firstStepStartProcessNotNullAddressTest() {

        ContactInformation contactInformation = ContactInformation.builder().build();
        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doReturn(EmptyReply.builder().build()).when(validateCatalogsContactUseCase).validateContactInfoCatalogs(anyList());

        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doNothing().when(contactIValidationUseCase).validateAddressType(anyList());
        doNothing().when(contactIValidationUseCase).validateForeignCountry(anyList());

        doReturn(contactInformationRepository).when(cInfoUCPer).getContactInformationRepository();
        doReturn(new ArrayList<>()).when(contactInformationRepository).findAllByAcquisition(any(Acquisition.class));
        doReturn(true).when(contactIValidationUseCase).existsTypeAddressResBrand(any(Acquisition.class)
                , anyList(), anyList());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        doReturn(new ArrayList<>()).when(contactInformationProcessUseCase).validateMandatory
                (any(ContactInformation.class), anyList(), anyString(), any(DependentFieldParamValidator.class));
        ArrayErrors arrayErrors = new ArrayErrors(new ArrayList<>());
        Acquisition acquisition =Acquisition.builder().id(UUID.randomUUID()).typeAcquisition(TypeAcquisition.builder()
                .id(UUID.randomUUID()).code("VT001").build()).build();
        List<ContactInformation> contactInformationList = contactInformationProcessUseCase.firstStepStartProcess
                (acquisition, Collections.singletonList(contactInformation), arrayErrors);
        assertNotNull(contactInformationList);
    }

    @Test
    public void firstStepStartProcessMandatoryTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doReturn(EmptyReply.builder().build()).when(validateCatalogsContactUseCase).validateContactInfoCatalogs(anyList());
        doReturn(new ArrayList<>()).when(contactInformationProcessUseCase)
                .validateMandatory(any(ContactInformation.class), anyList(),
                anyString(), any(DependentFieldParamValidator.class));

        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doNothing().when(contactIValidationUseCase).validateAddressType(anyList());
        doNothing().when(contactIValidationUseCase).validateForeignCountry(anyList());

        doReturn(contactInformationRepository).when(cInfoUCPer).getContactInformationRepository();
        doReturn(new ArrayList<>()).when(contactInformationRepository).findAllByAcquisition(any(Acquisition.class));
        doReturn(true).when(contactIValidationUseCase).existsTypeAddressResBrand(any(Acquisition.class)
                , anyList(), anyList());
        doNothing().when(vinculationUpdateUseCase).markOperation(any(UUID.class), anyString(), anyString());

        ArrayErrors arrayErrors = new ArrayErrors(new ArrayList<>());
        Acquisition acquisition =Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build()).build();
        List<ContactInformation> contactInformationList = contactInformationProcessUseCase.firstStepStartProcess
                (acquisition, Collections.singletonList(contactInformation), arrayErrors);
        assertNotNull(contactInformationList);

    }

    @Test(expected = ValidationException.class)
    public void firstStepStartProcessExceptionTest() {
        doReturn(contactIValidationUseCase).when(cInfoUCPer).getCValidationUseCase();
        doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doNothing().when(contactIValidationUseCase).validateAddressType(anyList());
        doNothing().when(contactIValidationUseCase).validateForeignCountry(anyList());
        doReturn(contactInformationRepository).when(cInfoUCPer).getContactInformationRepository();
        doReturn(new ArrayList<>()).when(contactInformationRepository).findAllByAcquisition(any(Acquisition.class));
        doReturn(false).when(contactIValidationUseCase).existsTypeAddressResBrand(any(Acquisition.class)
                , anyList(), anyList());
        ArrayErrors arrayErrors = new ArrayErrors(new ArrayList<>());
        Acquisition acquisition =Acquisition.builder().id(UUID.randomUUID()).typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build()).build();
        List<ContactInformation> contactInformation = contactInformationProcessUseCase
                .firstStepStartProcess(acquisition, new ArrayList<>(), arrayErrors);
    }
}