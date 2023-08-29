package co.com.bancolombia.sqs;

import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.parameters.ParametersUseCase;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ONE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.TWO;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class SqsMessageParameterUsesCaseTest {

	@InjectMocks
	@Spy
	private SqsMessageParameterUseCaseImpl sqsMessageParameterUseCase;

	@Mock
	private ParametersUseCase parametersUseCase;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void returnParameterMaxRetriesReadMessagePresentTest() {
		Parameters param = Parameters.builder().code("2").build();
		doReturn(Optional.of(param)).when(parametersUseCase).findByNameAndParent(anyString(), anyString());

		Integer r = sqsMessageParameterUseCase.returnParameterMaxRetriesReadMessage();
		assertEquals(TWO, r);
	}

	@Test
	public void returnParameterMaxRetriesReadMessageEmptyTest() {
		doReturn(Optional.empty()).when(parametersUseCase).findByNameAndParent(anyString(), anyString());

		Integer r = sqsMessageParameterUseCase.returnParameterMaxRetriesReadMessage();
		assertEquals(ONE, r);
	}
}
