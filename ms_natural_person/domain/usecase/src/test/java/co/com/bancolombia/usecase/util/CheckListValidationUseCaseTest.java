package co.com.bancolombia.usecase.util;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import co.com.bancolombia.model.basicinformation.BasicInformation;
import co.com.bancolombia.usecase.rabbit.vinculationupdate.VinculationUpdateUseCase;
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

@RequiredArgsConstructor
public class CheckListValidationUseCaseTest {

    @InjectMocks
    @Spy
    private ValidateMandatoryFields validateMandatoryFields;

    @Mock
    private VinculationUpdateUseCase vinculationUpdateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateMandatoryFields() {
        BasicInformation basicInformation = BasicInformation.builder().build();
        List<ExecFieldReply> execFieldReplies =
                Collections.singletonList(ExecFieldReply.builder().name("gender").build());
        List<ErrorField> errorFields = validateMandatoryFields
                .validateMandatoryFields(basicInformation, execFieldReplies);
        assertNotNull(errorFields);
    }
}
