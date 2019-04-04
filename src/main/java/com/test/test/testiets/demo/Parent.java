package com.test.test.testiets.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Parent {

    @RabbitListener(
            queues = {"test-queue"}
    )
    public void doSomething(){
        System.out.println("Luke I am your father");
    }
}
