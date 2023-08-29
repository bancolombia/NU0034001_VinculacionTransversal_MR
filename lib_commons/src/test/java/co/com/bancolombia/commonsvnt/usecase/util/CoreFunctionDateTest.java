package co.com.bancolombia.commonsvnt.usecase.util;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class CoreFunctionDateTest {

    @InjectMocks
    @Spy
    CoreFunctionDate coreFunctionDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getLocalDateTimeNowTest() {
        LocalDateTime.now();
        Date date = this.coreFunctionDate.getDatetime();
        assertNotNull(date);
    }

    @Test
    public void getDateFromStringTest() {
        String date = "20201001";
        String format = "yyyyMMdd";
        Date dateResult = this.coreFunctionDate.getDateFromString(date, format);
        assertNotNull(dateResult);
    }

    @Test
    public void getDateFromStringIsNullTest() {
        String format = "yyyyMMdd";
        Date dateResult = this.coreFunctionDate.getDateFromString(null, format);
        assertNull(dateResult);
    }

    @Test
    public void getDatetimeTest() {
        LocalDate localDate = this.coreFunctionDate.getLocalDateNow();
        assertNotNull(localDate);
    }

    @Test
    public void toDateStringTest() {
        Date dateResult = this.coreFunctionDate.toDate("2020-08-02 12:20:20");
        assertNotNull(dateResult);
    }

    @Test
    public void getLocalDateTimeFromStringTest() {
        LocalDateTime dateResult = this.coreFunctionDate.getLocalDateTimeFromString("2020-08-02 13:10:20");
        assertNotNull(dateResult);
    }

    @Test
    public void fromDateTest() {
        LocalDate localDate = this.coreFunctionDate.fromDate(new Date());
        assertNotNull(localDate);
    }

    @Test
    public void toDateLocalDateTimeTest() {
        Date date = this.coreFunctionDate.toDate(LocalDateTime.now());
        assertNotNull(date);
    }

    @Test
    public void compareDifferenceTimeTest() {
        String response = coreFunctionDate.compareDifferenceTime(
                coreFunctionDate.getDatetime(), "180", true, true);
        assertNotNull(response);
    }

    @Test
    public void minutesFormat() {
        String response = coreFunctionDate.minutesFormat(180);
        assertNotNull(response);
    }

    @Test
    public void localDateTimePlusTime() {
        String response = coreFunctionDate.localDateTimePlusTime(1,1,1,1);
        assertNotNull(response);
    }

    @Test
    public void toFormatDate() {
        String response = coreFunctionDate.toFormatDate(new Date());
        assertNotNull(response);
    }

    @Test
    public void returnAgeTest() {
        int response = coreFunctionDate.returnAge(coreFunctionDate.getDatetime());
        assertNotNull(response);
    }

    @Test
    public void dayUnLock() {
        LocalDateTime time = coreFunctionDate.dayUnLock(coreFunctionDate.getLocalDateTimeNow());
        assertNotNull(time);
    }

    @Test
    public void monthUnLock() {
        LocalDateTime time = coreFunctionDate.monthUnLock(coreFunctionDate.getLocalDateTimeNow());
        assertNotNull(time);
    }

    @Test
    public void yearUnLock() {
        LocalDateTime time = coreFunctionDate.yearUnLock(coreFunctionDate.getLocalDateTimeNow());
        assertNotNull(time);
    }

    @Test
    public void timeUnBlockCustomerBody() {
        LocalDateTime time = coreFunctionDate.getLocalDateTimeNow();
        doReturn(time).when(coreFunctionDate).yearUnLock(time);
        doReturn(time).when(coreFunctionDate).monthUnLock(time);
        doReturn(time).when(coreFunctionDate).dayUnLock(time);
        Date date = new Date();
        doReturn(date).when(coreFunctionDate).toDate(any(LocalDateTime.class));
    }

    @Test
    public void timeUnBlockCustomer1() {
        timeUnBlockCustomerBody();
        Date date = coreFunctionDate.timeUnBlockCustomer("1");
        assertNotNull(date);
    }

    @Test
    public void timeUnBlockCustomer2() {
        timeUnBlockCustomerBody();
        Date date = coreFunctionDate.timeUnBlockCustomer("2");
        assertNotNull(date);
    }

    @Test
    public void timeUnBlockCustomer3() {
        timeUnBlockCustomerBody();
        Date date = coreFunctionDate.timeUnBlockCustomer("3");
        assertNotNull(date);
    }

    @Test
    public void expirationTimeMillis() {
        Date date = new Date();
        doReturn(date).when(coreFunctionDate).getDatetime();
        Date date1 = coreFunctionDate.expirationTimeMillis(5);
        assertNotNull(date1);
    }
}