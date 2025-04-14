package com.example.reservationsystem.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092" );
        config.put( ConsumerConfig.GROUP_ID_CONFIG, "group_1" );
        config.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
        config.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
        return new DefaultKafkaConsumerFactory<>( config );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory( KafkaTemplate<?, ?> kafkaTemplate ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory( consumerFactory() );
        factory.setCommonErrorHandler( errorHandler( kafkaTemplate ) );
        factory.setRecordMessageConverter( new StringJsonMessageConverter() );
        return factory;
    }

    public CommonErrorHandler errorHandler( KafkaTemplate<?, ?> kafkaTemplate ) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                ( record, ex ) -> new TopicPartition( record.topic() + ".DLT", record.partition() ) );

        DefaultErrorHandler errorHandler = new DefaultErrorHandler( recoverer, new FixedBackOff( 2000L, 3 ) );

        errorHandler.setRetryListeners(( record, ex, deliveryAttempt ) -> {
            log.warn("⏳ Retrying (attempt {}): key={}, topic={}, error={}",
                    deliveryAttempt,
                    record.key(), record.topic(), ex.getMessage());
        });

        return errorHandler;
    }

}
