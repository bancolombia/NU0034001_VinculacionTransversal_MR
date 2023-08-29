package co.com.bancolombia.commonsvnt.common.exception;

import java.io.IOException;

public class CustomIOException extends IOException{
	private static final long serialVersionUID = -2911224585665408737L;

    public CustomIOException(String msg) {
        super(msg);
    }
}
