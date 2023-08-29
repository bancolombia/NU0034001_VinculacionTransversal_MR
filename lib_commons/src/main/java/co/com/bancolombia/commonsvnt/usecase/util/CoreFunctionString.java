package co.com.bancolombia.commonsvnt.usecase.util;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CoreFunctionString {

    public String upperCaseString(String string){
        if(string != null){
            string = string.toUpperCase();
        }
        return string;
    }

    public String lowerCaseString(String string){
        if(string != null){
            string = string.toLowerCase();
        }
        return string;
    }

    public String integerToString(Integer data){
        if(data != null){
            return Integer.toString(data);
        }
        return null;
    }

    public Integer stringToInteger(String data){
        if(data != null){
            return Integer.parseInt(data);
        }
        return null;
    }

    public BigDecimal stringToDecimal(String string){
        if(string != null){
            return new BigDecimal(string);
        }
        return null;
    }

    public String concatSecondStringOptional(String first, String second){
        if(second != null){
            return first.concat(" ").concat(second);
        }
        return first;
    }
}
