package co.com.bancolombia.usecase.personalinformation;

import co.com.bancolombia.commonsvnt.rabbit.common.reply.EmptyReply;
import co.com.bancolombia.model.personalinformation.PersonalInformation;
import co.com.bancolombia.usecase.util.UtilCatalogs;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class PersonalCatalogsUseCaseTest {

    @InjectMocks
    @Spy
    ValidateCatalogsPersonalUseCase validateCatalogsPersonalUseCase;

    @Mock
    UtilCatalogs utilCatalogs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

   @Test
    public void validatePersonalInfoCatalogs(){
        PersonalInformation personalInformation = PersonalInformation.builder().expeditionCountry("CO").
                expeditionDepartment("ANT").expeditionPlace("MDE").build();
        doReturn(EmptyReply.builder().build()).when(utilCatalogs)
                .callValidateCatalog(isNull(), anyList(), isNull(), anyString());
        EmptyReply reply = validateCatalogsPersonalUseCase.validatePersonalInfoCatalogs(personalInformation);
        assertNotNull(reply);
    }
}