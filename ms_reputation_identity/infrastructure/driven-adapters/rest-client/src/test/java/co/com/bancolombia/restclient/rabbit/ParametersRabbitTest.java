package co.com.bancolombia.restclient.rabbit;

import co.com.bancolombia.commonsvnt.rabbit.common.query.AcquisitionIdQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.query.ParameterQuery;
import co.com.bancolombia.commonsvnt.rabbit.common.reply.ParameterReply;
import co.com.bancolombia.commonsvnt.rabbit.reputationidentity.reply.IdentityValResultReply;
import co.com.bancolombia.model.expoquestion.ExpoQuestionSave;
import co.com.bancolombia.model.expoquestion.QuestionnaireSave;
import co.com.bancolombia.model.parameter.Parameter;
import co.com.bancolombia.model.parameter.gateways.ParametersRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class ParametersRabbitTest {
    @InjectMocks
    @Spy
    private ParametersRabbit parametersRabbit;

    @Mock
    private ParametersRepository parametersRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByNameParentSuccessTest() {
        ParameterQuery query = ParameterQuery.builder().name("asd").parent("asd").build();
        Parameter parameter = Parameter.builder().code("10").build();
        doReturn(Optional.of(parameter)).when(parametersRepository).findByNameParent(any(String.class),
                any(String.class));
        ParameterReply reply = parametersRabbit.findByNameParent(query);
        assertTrue(reply.isValid());
    }

    @Test
    public void getIndentityValResultReplyFalseTest() {
        ParameterQuery query = ParameterQuery.builder().name(null).parent(null).build();
        ParameterReply reply = parametersRabbit.findByNameParent(query);
        assertFalse(reply.isValid());
    }

    @Test
    public void getIndentityValResultReplyNullTest() {
        ParameterReply reply = parametersRabbit.findByNameParent(null);
        assertFalse(reply.isValid());
    }
}
