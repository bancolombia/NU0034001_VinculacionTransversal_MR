package co.com.bancolombia.foreigninformation;

import co.com.bancolombia.TestUtils;
import co.com.bancolombia.commonsvnt.api.model.util.MetaRequest;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionString;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequest;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequestData;
import co.com.bancolombia.foreigninformation.model.ForeignCurrencyInfoRequestDataList;
import co.com.bancolombia.model.foreigninformation.ForeignInformationOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ForeignInformationConstructControllerTest {


    @InjectMocks
    @Spy
    private CreateModelForeignOperation createModelForeignOperation;

    @Mock
    private CoreFunctionString coreFunctionString;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void contextLoads() {
        assertNotNull(createModelForeignOperation);
    }

    @Test
    public void creteModelForeignOperation()  {
        ForeignCurrencyInfoRequestData currencyInfoRequestData = ForeignCurrencyInfoRequestData.builder()
                .foreignCurrencyTransactions("S")
                .foreignCurrencyInfoRequestDataList(
                        Collections.singletonList(ForeignCurrencyInfoRequestDataList.builder()
                                .foreignCurrencyTransactionType("asd").productType("asd").which("asd")
                                .country("asd").department("asd").city("asd").nameEntity("asd")
                                .productNumber("asd").currency("asd").city("asd").averageMonthlyAmount("123")
                                .build())).build();
        MetaRequest meta = TestUtils.buildMetaRequest("foreign-currency-info");
        doReturn(new BigDecimal(2)).when(coreFunctionString).stringToDecimal(anyString());
        doReturn(new Date()).when(coreFunctionDate).getDatetime();
        ForeignCurrencyInfoRequest foreignCurrencyInfoRequest = new
                ForeignCurrencyInfoRequest(meta, currencyInfoRequestData);
        ForeignInformationOperation foreignInformationOperation = createModelForeignOperation
                .creteModelForeignOperation(foreignCurrencyInfoRequest, Acquisition.builder().build());
        assertNotNull(foreignInformationOperation);
    }
}