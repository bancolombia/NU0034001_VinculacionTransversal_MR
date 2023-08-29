package co.com.bancolombia.logfunctionalvnt.log.conf;

import org.springframework.core.env.Environment;

public class JpaConfigLogFunctional {
    private Environment parameters;

    public JpaConfigLogFunctional(Environment env) {
        this.parameters = env;
    }
    
    
}
