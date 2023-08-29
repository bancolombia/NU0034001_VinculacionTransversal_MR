package co.com.bancolombia.usecase.contactinformation;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.model.contactinformation.gateways.ContactInformationRepository;
import lombok.Data;
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
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@Data
@RequiredArgsConstructor
public class ContactInformationUseCasePersistTest {

    @InjectMocks
    @Spy
    private ContactInformationUseCasePersist contactInformationUseCasePersist;

    @Mock
    private ContactInformationRepository repository;

    @Mock
    private ContactIValidationUseCase cValidationUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findTest(){
        List<ContactInformation> contactInformation = new ArrayList<>();
        Mockito.doReturn(contactInformation).when(repository).findAllByAcquisition(any(Acquisition.class));
        contactInformationUseCasePersist.findAllByAcquisition(Acquisition.builder().build());
        assertNotNull(contactInformation);
    }

    @Test
    public void saveTest() {
        Mockito.doReturn(null).when(repository).findByAcquisitionAndAddressType(any(Acquisition.class), anyString());
        Mockito.doNothing().when(cValidationUseCase).maxRepetitionEmailCellphone(any(ContactInformation.class));
        Mockito.doReturn(ContactInformation.builder().build()).when(repository).save(any(ContactInformation.class));

        ContactInformation contactInformation = this.contactInformationUseCasePersist.save(ContactInformation.builder().build());
        assertNotNull(contactInformation);
    }

    @Test
    public void saveMergeTest() {
        Acquisition acquisition = Acquisition.builder().id(UUID.randomUUID()).build();
        ContactInformation contactInformation = ContactInformation.builder().addressType("Z001")
                .acquisition(acquisition).build();

        Mockito.doReturn(contactInformation).when(repository).findByAcquisitionAndAddressType(any(Acquisition.class), anyString());
        Mockito.doNothing().when(cValidationUseCase).maxRepetitionEmailCellphone(any(ContactInformation.class));
        Mockito.doReturn(contactInformation).when(repository).save(any(ContactInformation.class));

        ContactInformation information = this.contactInformationUseCasePersist.save(contactInformation);
        assertNotNull(information);
    }

    @Test
    public void saveAllTest() {
        List<ContactInformation> list = Collections.singletonList(ContactInformation.builder().build());

        Mockito.doReturn(list).when(repository).saveAll(anyList());

        List<ContactInformation> response = this.contactInformationUseCasePersist.save(list);
        assertNotNull(response);
    }

    @Test
    public void findByAcquisitionAndAddressType() {
        ContactInformation list = ContactInformation.builder().build();
        Mockito.doReturn(list).when(repository).findByAcquisitionAndAddressType(any(Acquisition.class), anyString());
        ContactInformation contactInformation = contactInformationUseCasePersist
                .findByAcquisitionAndAddressType(Acquisition.builder().build(), "CLASED_Z001");
        assertNotNull(contactInformation);
    }
}
