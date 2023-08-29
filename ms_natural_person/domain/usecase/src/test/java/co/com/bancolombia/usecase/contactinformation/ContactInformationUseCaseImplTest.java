package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.common.exception.ValidationException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.dependentfield.DependentFieldParamValidator;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import co.com.bancolombia.model.merge.MergeAttrib;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.contactinformation.generic.ArrayErrors;
import co.com.bancolombia.usecase.merge.MergeUseCase;
import co.com.bancolombia.usecase.personalinformation.PersonalInformationUseCase;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ContactInformationUseCaseImplTest {

    @InjectMocks
    @Spy
    ContactInformationUseCaseImpl contactInformationUseCase;

    @Mock
    ContactInformationProcessUseCase contactInformationProcessUseCase;

    @Mock
    ContactInformationRepository contactInformationRepository;

    @Mock
    ContactIValidationUseCase contactIValidationUseCase;


    @Mock
    PersonalInformationUseCase perInfoUseCase;

    @Mock
    MergeUseCase mergeUseCase;

    @Mock
    ContactInformationUseCasePersist contactInformationUseCasePersist;

    @Mock
    VinculationUpdateUseCase vinculationUpdateUseCase;

    @Mock
    private DependentFieldParamValidator depFieldParamVal;
    private Acquisition acquisition;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build()).build();
        depFieldParamVal = DependentFieldParamValidator.builder().acquisition(acquisition).operation("contact-information").build();

    }

    @Test
    public void saveNullTest() {
        ContactInformation contactInformation = ContactInformation.builder().acquisition(acquisition).build();
        doReturn(contactInformation).when(this.contactInformationUseCasePersist).save(any(ContactInformation.class));
        ContactInformation contactInfo = this.contactInformationUseCase.save(contactInformation);
        assertEquals(contactInformation.getAcquisition().getId().toString(),
                contactInfo.getAcquisition().getId().toString());

    }

    @Test
    public void saveTest() {
        ContactInformation contactInformation = ContactInformation.builder().acquisition(acquisition).build();
        doReturn(contactInformation).when(this.contactInformationUseCasePersist).save(any(ContactInformation.class));
        doReturn(contactInformation).when(this.contactInformationRepository).save(any(ContactInformation.class));
        ContactInformation contactInfo = this.contactInformationUseCase.save(contactInformation);
        assertEquals(contactInformation.getAcquisition().getId().toString(),
                contactInfo.getAcquisition().getId().toString());
    }

    @Test
    public void saveAllTest() {
        List<ContactInformation> list = Collections.singletonList(ContactInformation.builder().build());
        doReturn(list).when(contactInformationUseCase).startProcessContactInformation(anyList());
        List<ContactInformation> listResult = this.contactInformationUseCase.save(list);
        assertNotNull(listResult);
    }

    @Test
    public void constructContactInformationTest() {
        ContactInformation contactInformation = this.contactInformationUseCase.constructContactInformation(ContactInformation.builder().build());
        assertNotNull(contactInformation);
    }

    @Test
    public void constructMergeObjectTest() {
        MergeAttrib contactInformation = this.contactInformationUseCase.constructMergeObject(true, ContactInformation.builder().build());
        assertNotNull(contactInformation);
    }

    @Test
    public void concatErrorTypeDirectionTest() {
        doReturn("asd").when(contactInformationProcessUseCase)
                .concatErrorTypeDirection(anyString());
        String contactInformation = this.contactInformationUseCase.concatErrorTypeDirection("");
        assertNotNull(contactInformation);
    }

    @Test
    public void firstStepStartProcessTest() {
        List contactInformations = new ArrayList<>();
        ArrayErrors arrayErrors = ArrayErrors.builder().build();
        doReturn(contactInformations).when(contactInformationProcessUseCase)
                .firstStepStartProcess(any(Acquisition.class), any(List.class), any(ArrayErrors.class));
        contactInformations = this.contactInformationUseCase
                .firstStepStartProcess(acquisition, contactInformations, arrayErrors);
        assertNotNull(contactInformations);
    }

/*    @Test
    public void mandatoryExecFListTest() {
        Mockito.doReturn(new ArrayList<>()).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString())
                .getExecFieldList();
        List<ExecFieldReply> execFields = this.contactInformationUseCase.mandatoryExecFList(Acquisition.builder().build());
        assertNotNull(execFields);
    }*/

    @Test
    public void filterAddressTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        List<ContactInformation> contactInformations = Collections.singletonList(contactInformation);
        Optional<ContactInformation> contactInformationR = this.contactInformationUseCase.filterAddress(contactInformations, contactInformation);
        assertNotNull(contactInformationR.get());
    }

    @Test
    public void filterAddressNullTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        List<ContactInformation> contactInformations = Collections.singletonList(ContactInformation.builder().addressType("Z").build());
        Optional<ContactInformation> contactInformationR = this.contactInformationUseCase.filterAddress(contactInformations, contactInformation);
        assertFalse(contactInformationR.isPresent());
    }

    @Test
    public void changeUpdateOrCreateByNullOldTest() {
        ContactInformation contactInformation = ContactInformation.builder().build();
        ContactInformation result = this.contactInformationUseCase.changeUpdateOrCreateBy(contactInformation, null);
        assertNotNull(result);
    }

    @Test
    public void changeUpdateOrCreateByNullOldCreateDateTest() {
        ContactInformation contactInformation = ContactInformation.builder().build();
        ContactInformation oldContactInformation = ContactInformation.builder().createdBy("").build();
        ContactInformation result = this.contactInformationUseCase.changeUpdateOrCreateBy(contactInformation, oldContactInformation);
        assertNotNull(result);
    }

    @Test
    public void changeUpdateOrCreateByNullOldCreateByTest() {
        ContactInformation contactInformation = ContactInformation.builder().build();
        ContactInformation oldContactInformation = ContactInformation.builder().createdDate(new Date()).build();
        ContactInformation result = this.contactInformationUseCase.changeUpdateOrCreateBy(contactInformation, oldContactInformation);
        assertNotNull(result);
    }

    @Test
    public void changeUpdateOrCreateByOldNotNullByTest() {
        ContactInformation contactInformation = ContactInformation.builder().build();
        ContactInformation oldContactInformation = ContactInformation.builder().createdBy("")
                .createdDate(new Date()).build();
        ContactInformation result = this.contactInformationUseCase.changeUpdateOrCreateBy(contactInformation, oldContactInformation);
        assertNotNull(result);
    }

    @Test
    public void startProcessContactInformationNotAcquisitionTest() {
        List<ContactInformation> contactInformationList = Collections.singletonList(ContactInformation.builder()
                .acquisition(Acquisition.builder().build()).build());
        doReturn(new ArrayList<>()).when(contactInformationUseCase).firstStepStartProcess(any(Acquisition.class)
                , anyList(), any(ArrayErrors.class));
        doReturn(Optional.empty()).when(perInfoUseCase).findByAcquisition(any(Acquisition.class));
        Mockito.doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
/*        List<ContactInformation> contactInformations = this.contactInformationUseCase.startProcessContactInformation(contactInformationList);
        assertNotNull(contactInformations);*/
    }

    @Test
    public void startProcessContactInformationPiPresentTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        List<ContactInformation> contactInformationList = Collections.singletonList(ContactInformation.builder().addressType("Z")
                .acquisition(Acquisition.builder().build()).build());
        PersonalInformation personalInformation = PersonalInformation.builder().build();
        doReturn(new ArrayList<>()).when(contactInformationUseCase).firstStepStartProcess(any(Acquisition.class)
                , anyList(), any(ArrayErrors.class));
        doReturn(Optional.of(personalInformation)).when(perInfoUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(Optional.of(contactInformation)).when(contactInformationUseCase).filterAddress(anyList(), any(ContactInformation.class));
        Mockito.doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doReturn(new ArrayList<>()).when(mergeUseCase).merge(any(ContactInformation.class), any(ContactInformation.class), any(MergeAttrib.class));
        doReturn(contactInformation).when(contactInformationUseCase).changeUpdateOrCreateBy(any(ContactInformation.class), any(ContactInformation.class));
        doReturn(false).when(contactIValidationUseCase).validateFieldsContactInfoNull(any(ContactInformation.class));
        /*List<ContactInformation> contactInformations = this.contactInformationUseCase.startProcessContactInformation(contactInformationList);
        assertNotNull(contactInformations);

    }

    @Test
    public void startProcessContactInformationEqualAddressTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        List<ContactInformation> contactInformationList = Collections.singletonList(ContactInformation.builder().addressType("Z001")
                .acquisition(Acquisition.builder().build()).build());
        Mockito.doReturn(new ArrayList<>()).when(contactInformationUseCase).firstStepStartProcess(any(Acquisition.class)
                , anyList(), any(ArrayErrors.class));
        Mockito.doReturn(Optional.empty()).when(perInfoUseCase).findByAcquisition(any(Acquisition.class));
        Mockito.doReturn(Optional.of(contactInformation)).when(contactInformationUseCase).filterAddress(anyList(), any(ContactInformation.class));
        Mockito.doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        Mockito.doReturn(new ArrayList<>()).when(mergeUseCase).merge(any(ContactInformation.class), any(ContactInformation.class), any(MergeAttrib.class));
        Mockito.doReturn(contactInformation).when(contactInformationUseCase).changeUpdateOrCreateBy(any(ContactInformation.class), any(ContactInformation.class));
        Mockito.doReturn(false).when(contactIValidationUseCase).validateFieldsContactInfoNull(any(ContactInformation.class));
        /*List<ContactInformation> contactInformations = this.contactInformationUseCase.startProcessContactInformation(contactInformationList);
        assertNotNull(contactInformations);
        */
    }


    @Test
    public void startProcessContactInformationNotMergeTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        List<ContactInformation> contactInformationList = Collections.singletonList(ContactInformation.builder().addressType("Z001")
                .acquisition(Acquisition.builder().build()).build());
        PersonalInformation personalInformation = PersonalInformation.builder().build();
        doReturn(new ArrayList<>()).when(contactInformationUseCase).firstStepStartProcess(any(Acquisition.class)
                , anyList(), any(ArrayErrors.class));
        doReturn(Optional.of(personalInformation)).when(perInfoUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(Optional.of(contactInformation)).when(contactInformationUseCase).filterAddress(anyList(), any(ContactInformation.class));
        Mockito.doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doReturn(new ArrayList<>()).when(mergeUseCase).merge(any(ContactInformation.class), any(ContactInformation.class), any(MergeAttrib.class));
        doReturn(contactInformation).when(contactInformationUseCase).changeUpdateOrCreateBy(any(ContactInformation.class), any(ContactInformation.class));
        doReturn(false).when(contactIValidationUseCase).validateFieldsContactInfoNull(any(ContactInformation.class));
        /*List<ContactInformation> contactInformations = this.contactInformationUseCase.startProcessContactInformation(contactInformationList);
        assertNotNull(contactInformations);*/
    }

    @Test
    public void startProcessContactInformationMergeTest() {
        ContactInformation contactInformation = ContactInformation.builder().addressType("").build();
        List<ContactInformation> contactInformationList = Collections.singletonList(ContactInformation.builder().addressType("Z001")
                .acquisition(Acquisition.builder().build()).build());
        PersonalInformation personalInformation = PersonalInformation.builder().build();
        doReturn(new ArrayList<>()).when(contactInformationUseCase).firstStepStartProcess(any(Acquisition.class)
                , anyList(), any(ArrayErrors.class));
        doReturn(Optional.of(personalInformation)).when(perInfoUseCase).findByAcquisition(any(Acquisition.class));
        doReturn(Optional.of(contactInformation)).when(contactInformationUseCase).filterAddress(anyList(), any(ContactInformation.class));
        Mockito.doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        doReturn(new ArrayList<>()).when(mergeUseCase).merge(any(ContactInformation.class), any(ContactInformation.class), any(MergeAttrib.class));
        doReturn(contactInformation).when(contactInformationUseCase).changeUpdateOrCreateBy(any(ContactInformation.class), any(ContactInformation.class));
        doReturn(true).when(contactIValidationUseCase).validateFieldsContactInfoNull(any(ContactInformation.class));
        doReturn(new ArrayList<>()).when(contactIValidationUseCase).validateIfRecordUpgradeable(any(Optional.class), any(Optional.class));
/*        List<ContactInformation> contactInformations = this.contactInformationUseCase.startProcessContactInformation(contactInformationList);
        assertNotNull(contactInformations);*/
    }

    @Test
    public void mergeFieldsContactInfoTest(){

        ContactInformation contactInformationInDB = ContactInformation.builder().build();
        ContactInformation contactInformationNew = ContactInformation.builder().build();
        List<ErrorField> errorFields = new ArrayList<>();
        doReturn(errorFields).when(mergeUseCase).merge(any(ContactInformation.class),any(ContactInformation.class),any(MergeAttrib.class));

        this.contactInformationUseCase.mergeFieldsContactInfo(contactInformationInDB,contactInformationNew);
        Mockito.verify(this.contactInformationUseCase,Mockito.times(1)).mergeFieldsContactInfo(contactInformationInDB,contactInformationNew);

    }

    @Test (expected = ValidationException.class)
    public void mergeFieldsContactInfoExceptionTest(){
        ContactInformation contactInformationInDB = ContactInformation.builder().build();
        ContactInformation contactInformationNew = ContactInformation.builder().build();
        List<ErrorField> errorFields = Collections.singletonList(ErrorField.builder().build());

        doReturn(errorFields).when(mergeUseCase).merge(any(ContactInformation.class),any(ContactInformation.class),any(MergeAttrib.class));
        this.contactInformationUseCase.mergeFieldsContactInfo(contactInformationInDB,contactInformationNew);

    }

/*    @Test
    public void startProcessContactInformationTest(){
        ArrayErrors arrayErrors = new ArrayErrors(new ArrayList<>());
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().addressType("").acquisition(acquisition).build();
        Mockito.doReturn(Collections.singletonList(contactInformation)).when(contactInformationUseCase).updateInfoObjectNew(anyList());
        Mockito.doReturn(new ArrayList<>()).when(contactInformationUseCase).mandatoryExecFList(any(Acquisition.class));
        Mockito.doReturn(new ArrayList<>()).when(vinculationUpdateUseCase).checkListStatus(any(UUID.class), anyString())
                .getExecFieldList();
        Mockito.doReturn(arrayErrors).when(contactInformationUseCase).eachListContactInformation(anyList(), any(ArrayErrors.class));
        Mockito.doReturn(contactIValidationUseCase).when(contactInformationUseCasePersist).getCValidationUseCase();
        Mockito.doNothing().when(contactIValidationUseCase).validateIfErrorField(anyList(), anyList());
        List<ContactInformation> contactInfo = this.contactInformationUseCase.startProcessContactInformation(
                Collections.singletonList(contactInformation));
        assertNotNull(contactInfo);

    }*/

    @Test
    public void updateInfoObjectNewTest () {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().addressType("").acquisition(acquisition).build();
        doReturn(contactInformationRepository).when(contactInformationUseCasePersist).getContactInformationRepository();
        doReturn(contactInformation).when(contactInformationRepository).findByAcquisitionAndAddressType(any(Acquisition.class),anyString());
        List<ContactInformation> contactInfo = this.contactInformationUseCase.updateInfoObjectNew(
                Collections.singletonList(contactInformation));
        assertNotNull(contactInfo);
    }

    @Test
    public void updateInfoObjectNewNUllTest () {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().addressType("").acquisition(acquisition).build();
        doReturn(contactInformationRepository).when(contactInformationUseCasePersist).getContactInformationRepository();
        doReturn(null).when(contactInformationRepository).findByAcquisitionAndAddressType(any(Acquisition.class),anyString());
        List<ContactInformation> contactInfo = this.contactInformationUseCase.updateInfoObjectNew(
                Collections.singletonList(contactInformation));
        assertNotNull(contactInfo);
    }

    @Test
    public void updateInfoObjectNewNotNullTest () {

        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().addressType("").brand("").acquisition(acquisition).build();
        doReturn(contactInformationRepository).when(contactInformationUseCasePersist).getContactInformationRepository();
        doReturn(contactInformation).when(contactInformationRepository).
                findByAcquisitionAndAddressType(any(Acquisition.class),anyString());
        Mockito.doNothing().when(contactInformationUseCase).mergeFieldsContactInfo(any(ContactInformation.class),
                any(ContactInformation.class));


        List<ContactInformation> contactInfo = this.contactInformationUseCase.updateInfoObjectNew(
                Collections.singletonList(contactInformation));
        assertNotNull(contactInfo);
    }
}