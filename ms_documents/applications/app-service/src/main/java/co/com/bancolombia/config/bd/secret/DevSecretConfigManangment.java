package co.com.bancolombia.config.bd.secret;

import co.com.bancolombia.model.commons.secretsmodel.SecretsModel;
import co.com.bancolombia.model.commons.secretsmodel.gateways.SecretsManagerConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "dev-local", "test" })
@Qualifier("secretConfig")
public class DevSecretConfigManangment implements SecretConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    @Value("${db.manangment.host}")
    private String host;

    @Value("${db.manangment.username}")
    private String userName;

    @Value("${db.manangment.password}")
    private String password;

    @Value("${db.manangment.port}")
    private Integer port;

    @Value("${db.manangment.bdname}")
    private String dbName;

    @Value("${db.manangment.url}")
    private String dbConnectionString;

    @Override
    public SecretsModel getModel(SecretsManagerConsumer<SecretsModel> consumer) {
        return SecretsModel.builder().username(userName).password(password).host(host)
                .dbname(dbName).port(port).engine(dbDriver).build();
    }

    @Override
    public String getDbConnectionString(SecretsModel secretsModel) {
        return String.format(this.dbConnectionString, secretsModel.getHost(),
                secretsModel.getPort(),
                secretsModel.getDbname());
    }
}