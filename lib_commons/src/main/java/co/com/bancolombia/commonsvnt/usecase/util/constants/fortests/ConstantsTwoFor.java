package co.com.bancolombia.commonsvnt.usecase.util.constants.fortests;
import co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper=false)
public class ConstantsTwoFor {

    public boolean validateConstantsTwo(String msg){
        return msg.equals(ConstantsTwo.SD_RESPONSE_CODE);
    }

    public boolean validateConstantsTwoArray(String[] array){
        return Arrays.equals(array,ConstantsTwo.ERRORS_CODES_NOTIFICATIONS_RETRY);
    }
}
