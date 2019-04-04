package com.test.test.testiets.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableConfigurationProperties({RabbitProperties.class})
public class RabbitAutoConfiguration {

    private RabbitProperties rabbitProperties;


    public RabbitAutoConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() throws Exception {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(20);
        factory.setPrefetchCount(10);
        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }


    @Bean("rabbitTemplate")
    @Primary
    @DependsOn(value = {"connectionFactory"})
    public RabbitTemplate rabbitTemplate() {
        try {
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
            ObjectMapper objectMapper = new ObjectMapper();
            JavaTimeModule javaTimeModule=new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
            objectMapper.registerModule(javaTimeModule);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
            rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
            return rabbitTemplate;
        } catch (Exception e) {
        }
        return null;
    }



    @Bean(name = "connectionFactory")
    @DependsOn(value = "rabbitClientConnectionFactory")
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() throws Exception {
        return new CachingConnectionFactory(rabbitConnectionFactory());
    }

    @Bean(name = "rabbitClientConnectionFactory")
    public ConnectionFactory rabbitConnectionFactory() throws Exception {
        RabbitConnectionFactoryBean rabbitConnectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBean.setHost("localhost");
        rabbitConnectionFactoryBean.setUsername("admin");
        rabbitConnectionFactoryBean.setPassword("admin");
        rabbitConnectionFactoryBean.setVirtualHost("/");
        rabbitConnectionFactoryBean.setPort(5672);
        rabbitConnectionFactoryBean.afterPropertiesSet();
        return rabbitConnectionFactoryBean.getObject();
    }
}

