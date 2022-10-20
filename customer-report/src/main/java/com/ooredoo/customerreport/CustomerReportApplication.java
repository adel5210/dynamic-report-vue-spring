package com.ooredoo.customerreport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CustomerReportApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(CustomerReportApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
