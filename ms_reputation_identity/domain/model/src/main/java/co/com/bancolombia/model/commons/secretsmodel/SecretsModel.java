package co.com.bancolombia.model.commons.secretsmodel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SecretsModel {
    private String username;
    private String password;
    private String host;
    private String dbname;
    private Integer port;
    private String engine;
    private String dbInstanceIdentifier;
}
