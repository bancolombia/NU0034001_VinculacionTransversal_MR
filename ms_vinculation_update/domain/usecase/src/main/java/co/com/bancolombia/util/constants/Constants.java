package co.com.bancolombia.util.constants;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_BASIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_CONTACT_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_ECONOMIC_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_FOREIGN_INFORMATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_PERSONAL_INFO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CODE_TAX_INFO;

public class Constants {
    public static final String ACQUISITION = "ADQUISICION";
    public static final String STEP = "OPERACION";
    public static final String STATE_STEP = "ESTADO OPERACION";
    public static final String CHECKLIST = "CHECKLIST";

    public static final String ERROR_MSG_INCOMPLETE_FIELDS = "Los datos del query están incompletos: ";
    public static final String ERROR_MSG_WRONG_UUID = "El acquisitionId %s no tiene el formato correcto";

    public static final String ERROR_MSG_STEP_NOT_EXISTS = "La operación %s no existe";
    public static final String ERROR_MSG_CHECKLIST_NOT_EXISTS = "El checklist no existe";
    public static final String ERROR_MSG_STATE_STEP_NOT_EXISTS = "El estado de operación %s no existe";
    public static final String ERROR_MSG_CLAUSE_CODE_NOT_EXISTS = "El código de clausula no existe";

    /**
     * Operations natural and legal person
     */
    public static final String[] opeNatLegPerson = {CODE_PERSONAL_INFO, CODE_BASIC_INFO, CODE_CONTACT_INFO,
            CODE_ECONOMIC_INFO, CODE_TAX_INFO, CODE_FOREIGN_INFORMATION};
}
