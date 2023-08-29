package co.com.bancolombia.logtechnicalvnt.log;

import org.junit.Test;

public class LoggerAdapterTest {

    @Test
    public void testLoggerAdapterDebug(){
        LoggerAdapter loggerAdapter = new LoggerAdapter();
        loggerAdapter.debug("");
    }

    @Test
    public void testLoggerAdapterError(){
        LoggerAdapter loggerAdapter = new LoggerAdapter("","","");
        loggerAdapter.error("");
        loggerAdapter.error("",new Exception());
    }

    @Test
    public void testLoggerAdapterInfo(){
        LoggerAdapter loggerAdapter = new LoggerAdapter();
        loggerAdapter.info("");
    }

    @Test
    public void testLoggerAdapterWarn(){
        LoggerAdapter loggerAdapter = new LoggerAdapter("","","");
        loggerAdapter.warn("");
        loggerAdapter.warn("",new Exception());
    }

    @Test
    public void testLoggerAdapterFatal(){
        LoggerAdapter loggerAdapter = new LoggerAdapter("","","");
        loggerAdapter.fatal("");
        loggerAdapter.fatal("",new Exception());
    }



}