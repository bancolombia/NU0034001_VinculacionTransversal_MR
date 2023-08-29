package co.com.bancolombia.config;

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class SqsConfListen {

    @Value("${aws.sqs.queueUrl}")
    private String sqsUrl;

    @Value("${aws.sqs.rolArn}")
    private String rolArn;

    @Value("${aws.sqs.waitTimeOut}")
    private int sqsWaitTimeOut;

    @Value("${aws.sqs.maxNumberOfMessages}")
    private int sqsMaxNumberOfMessages;

    @Bean
    @Primary
    @Profile({"dev", "qa", "pdn"})
    public AmazonSQSAsync amazonSqsAsync() {
        final var clientBuilder = AmazonSQSAsyncClientBuilder.standard();
        AmazonSQSAsyncClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(sqsUrl, Regions.US_EAST_1.getName());
        clientBuilder.setEndpointConfiguration(endpointConfiguration);
        clientBuilder.withCredentials(WebIdentityTokenCredentialsProvider.builder().roleArn(rolArn).build());
        return clientBuilder.build();
    }

    @Bean
    @Primary
    @Profile({"dev-local", "test"})
    public AmazonSQSAsync amazonSqsAsyncTest() {
        final var clientBuilder = AmazonSQSAsyncClientBuilder.standard();
        AmazonSQSAsyncClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(sqsUrl, Regions.US_EAST_1.getName());
        clientBuilder.setEndpointConfiguration(endpointConfiguration);
        return clientBuilder.build();
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
            AmazonSQSAsync amazonSqsAsync) {
        final var listenerContainerFactory = new SimpleMessageListenerContainerFactory();
        listenerContainerFactory.setAutoStartup(true);
        listenerContainerFactory.setAmazonSqs(amazonSqsAsync);
        listenerContainerFactory.setMaxNumberOfMessages(this.sqsMaxNumberOfMessages);
        listenerContainerFactory.setWaitTimeOut(this.sqsWaitTimeOut);
        return listenerContainerFactory;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
}
