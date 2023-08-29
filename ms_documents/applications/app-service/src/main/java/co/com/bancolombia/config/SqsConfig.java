package co.com.bancolombia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

    @Bean
    @Primary
    @Profile({"dev", "qa", "pdn"})
    public SqsAsyncClient initSqsClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .region(Region.US_EAST_1).build();
    }

    @Bean
    @Primary
    @Profile({"dev-local", "test"})
    public SqsAsyncClient initSqsClientLocal() {
        return SqsAsyncClient.create();
    }
}
