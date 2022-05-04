package org.rrajesh1979.devicedatapipeline;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeviceDataPipelineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceDataPipelineApplication.class, args);
    }

    // injected from application.properties
    @Value("${config.topic.name}")
    private String topicName;

    @Value("${config.topic.partitions}")
    private int numPartitions;

    @Value("${config.topic.replicas}")
    private int replicas;

    @Bean
    NewTopic moviesTopic() {
        return new NewTopic(topicName, numPartitions, (short) replicas);
    }

}
