package co.com.bancolombia.commonsvnt.common.exception;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = -2911224585665408737L;

    public CustomException(String msg) {
        super(msg);
    }
}