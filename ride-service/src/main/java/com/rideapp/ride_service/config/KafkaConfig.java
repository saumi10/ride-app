package com.rideapp.ride_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    //Topic where Ride Service publishes ride request
    // Matching Service subcribes to this topic

    @Bean
    public NewTopic rideRequestedTopic(){
        // TopicBuilder is a fluent API to create Kafka topics with specific configurations.
        //partitions: Number of partitions for the topic. More partitions allow for higher parallelism and throughput, means the topic will be divided into 3 partitions, allowing multiple consumers to read from the topic concurrently.
        //replicas: Number of replicas for the topic. Replication provides fault tolerance by duplicating data across multiple brokers.
        return TopicBuilder.name("ride.requested")
                .partitions(3)
                .replicas(1)
                .build();
    }

    //Topic where Matching Service publishes match results
    // Ride Service subscribes to this topic

    @Bean
    public NewTopic rideMatchedTopic(){
        return TopicBuilder.name("ride.matched")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
