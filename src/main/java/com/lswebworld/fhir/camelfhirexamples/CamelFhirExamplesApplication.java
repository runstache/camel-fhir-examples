package com.lswebworld.fhir.camelfhirexamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.lswebworld.fhir")
public class CamelFhirExamplesApplication {

  public static void main(String[] args) {
    SpringApplication.run(CamelFhirExamplesApplication.class, args);
  }

}
