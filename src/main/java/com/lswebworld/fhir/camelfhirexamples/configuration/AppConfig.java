package com.lswebworld.fhir.camelfhirexamples.configuration;

import com.lswebworld.fhir.camelfhirexamples.processors.BundleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean(name = "BundleProcessor")
  public BundleProcessor bundleProcessor() {
    return new BundleProcessor();
  }
}