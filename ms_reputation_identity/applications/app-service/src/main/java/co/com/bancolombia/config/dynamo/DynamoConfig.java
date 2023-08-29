package co.com.bancolombia.config.dynamo;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "qa", "pdn"})
@EnableDynamoDBRepositories
        (dynamoDBMapperConfigRef = "dynamoDBMapperConfig", basePackages = "co.com.bancolombia.jpa")
public class DynamoConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String dynamodbEndpoint;

    @Value("${aws.dynamodb.prefix}")
    private String tablePrefixDynamo;

    @Bean
    public DynamoDBMapperConfig.TableNameOverride tableNameOverride() {
        return DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(tablePrefixDynamo);
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride tableNameOverride) {
        return DynamoDBMapperConfig.builder().withTableNameOverride(tableNameOverride).build();
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(dynamodbEndpoint, Regions.US_EAST_1.getName()))
                .withCredentials(new WebIdentityTokenCredentialsProvider()).build();
    }

}
