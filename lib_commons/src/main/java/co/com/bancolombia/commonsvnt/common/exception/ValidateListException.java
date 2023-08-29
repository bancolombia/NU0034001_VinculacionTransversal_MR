package co.com.bancolombia.commonsvnt.common.exception;

import java.util.List;

public class ValidateListException extends RuntimeException {

    private static final long serialVersionUID = -5693452706859202868L;
    private List<InvalidOptionalArgumentException> listException;

    public ValidateListException(List<InvalidOptionalArgumentException> listException) {
        super();
        this.listException = listException;
    }

    public List<InvalidOptionalArgumentException> getListException() {
        return listException;
    }
}
