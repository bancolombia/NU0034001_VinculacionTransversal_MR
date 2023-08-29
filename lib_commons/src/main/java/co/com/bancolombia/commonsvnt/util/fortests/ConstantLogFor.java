package co.com.bancolombia.commonsvnt.util.fortests;

import co.com.bancolombia.commonsvnt.util.ConstantLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ConstantLogFor extends ConstantLog{
    
    public boolean validateConstantLog(String msg){
        return msg.equals(ConstantLog.LOGFIELD_USER);
    }
}
