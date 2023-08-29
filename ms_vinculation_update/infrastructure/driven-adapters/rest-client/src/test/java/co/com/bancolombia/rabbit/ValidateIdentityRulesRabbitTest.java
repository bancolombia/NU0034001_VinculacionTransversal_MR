package co.com.bancolombia.rabbit;

import co.com.bancolombia.acquisition.AcquisitionUseCase;
import co.com.bancolombia.commonsvnt.common.exception.CustomException;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.model.validateidentityrules.ValidateIdentityRules;
import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ValidateIdentityRulesReply;
import co.com.bancolombia.model.validateidentityrules.gateways.ValidateIdentityRulesRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class ValidateIdentityRulesRabbitTest {
    
    @InjectMocks
    @Spy
    private ValidateIdentityRulesRabbit validateIdentityRulesRabbit;
    
    @Mock
    private AcquisitionUseCase acquisitionUseCase;

    @Mock
    private ValidateIdentityRulesRepository validateIdentityRulesRepository;
    
    private UUID uuid;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uuid = UUID.randomUUID();
    }

    @Test
    public void validateIdentityRulesSuccessTest(){
        TypeAcquisition typeAcquisition = TypeAcquisition.builder().code("VT001").build();
        Acquisition acquisition = Acquisition.builder().id(uuid).typeAcquisition(typeAcquisition).build();
        doReturn(acquisition).when(acquisitionUseCase).findById(any(UUID.class));

        List<ValidateIdentityRules> validateIdentityRulesList = new ArrayList<>();

        ValidateIdentityRules validateIdentityRule = ValidateIdentityRules.builder().active(true)
                .rule("RuleOneCellphone").score(new BigDecimal("26.70")).build();
        validateIdentityRulesList.add(validateIdentityRule);

        doReturn(validateIdentityRulesList).when(validateIdentityRulesRepository)
                .findByTypeAcquisition(any(TypeAcquisition.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityRulesReply reply = validateIdentityRulesRabbit.validateIdentityRules(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void validateIdentityRulesCustomExceptionTest() {
        doThrow(CustomException.class).when(acquisitionUseCase).findById(any(UUID.class));

        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();

        ValidateIdentityRulesReply reply = validateIdentityRulesRabbit.validateIdentityRules(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityRulesNullQueryTest(){
        ValidateIdentityRulesReply reply = validateIdentityRulesRabbit.validateIdentityRules(null);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityRulesNullDataTest(){
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().build();
        ValidateIdentityRulesReply reply = validateIdentityRulesRabbit.validateIdentityRules(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityRulesEmptyDataTest(){
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId("").build();
        ValidateIdentityRulesReply reply = validateIdentityRulesRabbit.validateIdentityRules(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void validateIdentityRulesNullAcquisitionTest(){
        doReturn(null).when(acquisitionUseCase).findById(any(UUID.class));
        AcquisitionIdQuery query = AcquisitionIdQuery.builder().acquisitionId(uuid.toString()).build();
        ValidateIdentityRulesReply reply = validateIdentityRulesRabbit.validateIdentityRules(query);
        assertFalse(reply.isValid());
    }


}
