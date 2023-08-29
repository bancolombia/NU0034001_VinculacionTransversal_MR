package co.com.bancolombia.logtechnicalvnt.log;

import org.junit.Test;

public class LoggerModelTest {

    @Test
    public void testLoggerModel(){
        LoggerModel loggerModel1 = new LoggerModel("","","","");
        LoggerModel loggerModel2 = new LoggerModel("","","",new Exception());
    }

}