package co.com.bancolombia.usecase.contactinformation;


import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.model.contactinformation.ContactInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ContactCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    ValidateCatalogsContactUseCase validateCatalogsContactUseCase;

    @Mock
    UtilCatalogs utilCatalogs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateContactInfoCatalogsTest() {
        List<ContactInformation> contactInformationList = Collections.singletonList(ContactInformation.builder()
                .addressType("CLASED_Z001")
                .brand("MARCAC_XXDEFAULT").city("CIUDAD_76111000").department("DPTO_CO_000076")
                .country("PAIS_CO").build());
        EmptyReply reply = EmptyReply.builder().build();
        doReturn(reply).when(utilCatalogs)
                .callValidateCatalog(anyList(), anyList(), anyString(), anyString());
        reply = validateCatalogsContactUseCase.validateContactInfoCatalogs(contactInformationList);
        assertNotNull(reply);
    }
}