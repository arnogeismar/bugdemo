package com.test.test.testiets.demo;

import org.springframework.stereotype.Service;

@Service
public class Child extends Parent {


    public void doSomething(){
        System.out.println("Oh sweet summer child");
    }
}
