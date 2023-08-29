package co.com.bancolombia.customercontrol;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.typeacquisition.TypeAcquisition;
import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.customercontrol.CustomerControl;
import co.com.bancolombia.model.customercontrol.gateways.CustomerControlRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class CustomerControlUseCaseTest {

    @InjectMocks
    @Spy
    private CustomerControlUseCaseImpl customerControlUseCase;

    @Mock
    private CustomerControlRepository repository;

    @Mock
    private CoreFunctionDate coreFunctionDate;

    private Acquisition acquisition;
    private Date date;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        date = new Date();
        acquisition = Acquisition.builder().id(UUID.randomUUID())
                .typeAcquisition(TypeAcquisition.builder().id(UUID.randomUUID()).code("VT001").build())
                .build();
    }

    @Test
    public void findByDocNumberTest(){
        CustomerControl customerControl = CustomerControl.builder().build();
        doReturn(customerControl).when(repository).findByDocumentTypeAndDocumentNumber(anyString(), anyString());
        customerControl = customerControlUseCase.findByDocumentTypeAndDocumentNumber("asd", "asd");
        assertNotNull(customerControl);
    }

    @Test
    public void saveTest(){
        CustomerControl customerControl = CustomerControl.builder().build();
        doReturn(customerControl).when(repository).save(any(CustomerControl.class));
        customerControl = customerControlUseCase.save(customerControl);
        assertNotNull(customerControl);
    }

    @Test
    public void blockCustomerTest(){
        CustomerControl customerControl = CustomerControl.builder().build();
        doReturn(customerControl).when(customerControlUseCase).save(any(CustomerControl.class));
        Mockito.doReturn(date).when(coreFunctionDate).getDatetime();
        customerControlUseCase.blockCustomer("asd", "asd",
                coreFunctionDate.getDatetime(), "asd");
        verify(customerControlUseCase, times(1)).save(any(CustomerControl.class));
    }

    @Test
    public void unblockCustomerTest(){
        CustomerControl customerControl = CustomerControl.builder().state("1").unlockDate(date).build();
        Mockito.doReturn("02:59:00").when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));
        doReturn(customerControl).when(customerControlUseCase).save(any(CustomerControl.class));
        Mockito.doReturn(date).when(coreFunctionDate).getDatetime();
        customerControlUseCase.unblockCustomer(customerControl);
        verify(customerControlUseCase, times(1)).unblockCustomer(any(CustomerControl.class));
    }

    @Test
    public void unblockCustomerStateTest(){
        CustomerControl customerControl = CustomerControl.builder().state("2").unlockDate(date).build();
        String asd = customerControlUseCase.unblockCustomer(customerControl);
        assertNull(asd);
    }

    @Test
    public void unblockCustomerTimeBlockTest(){
        CustomerControl customerControl = CustomerControl.builder()
                .state("1")
                .unlockDate(new CoreFunctionDate().getDateFromString("01/01/2999", "dd/MM/yyyy")).build();

        Mockito.doReturn("02:59:00").when(coreFunctionDate)
                .compareDifferenceTime(any(Date.class), anyString(), any(Boolean.class), any(Boolean.class));
        doReturn(customerControl).when(customerControlUseCase).save(any(CustomerControl.class));
        Mockito.doReturn(date).when(coreFunctionDate).getDatetime();
        String asd = customerControlUseCase.unblockCustomer(customerControl);
        assertNotNull(asd);
    }
}
