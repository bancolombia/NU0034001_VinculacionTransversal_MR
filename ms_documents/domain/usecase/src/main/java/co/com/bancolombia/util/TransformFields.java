package co.com.bancolombia.util;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CAT_EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CAT_FLOOR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CAT_S;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.CAT_Z;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.LAST_TWO;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.MARK_CORR;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.NUMBER_SN;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.ZETA_SN;

@RequiredArgsConstructor
public class TransformFields {

    private final CoreFunctionDate coreFunctionDate;

    public String transform(String str, String format) {
        if(str == null || str.isEmpty()) {
            return null;
        }
        str = str.substring(str.lastIndexOf(CAT_FLOOR) + 1);
        if (format != null) {
            switch (format) {
                case NUMBER_SN:
                    str = CAT_S.equals(str) ? Numbers.ONE.getNumber() : Numbers.TWO.getNumber();
                    break;
                case ZETA_SN:
                    str = CAT_Z.concat(str);
                    break;
                case MARK_CORR:
                    str = CAT_EMPTY.equals(str) ? Numbers.ZERO.getNumber() : Numbers.ONE.getNumber();
                    break;
                case LAST_TWO:
                    str = str.substring(Numbers.FOUR.getIntNumber());
                    break;
                default:
                    break;
            }
        }
        return str;
    }

    public String modDNN(Date date) {
        if(date == null) {
            return null;
        }
        return coreFunctionDate.toFormatDate(date);
    }
}