package com.lswebworld.fhir.camelfhirexamples.builders;

import ca.uhn.hl7v2.HL7Exception;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Camel Route Builder that will ingest a file
 * from a location that contains a single HL7 Message,
 * transform it into a Fhir Bundle containing a patient,
 * encounter, and encounter locations then send it to 
 * a FHIR server using the Producer Component.
 */
@Component
public class BundleRouteBuilder extends RouteBuilder {

  @Override
  public void configure() {
    onException(HL7Exception.class)
      .log("HL7 Exception");
    
    from("file://{{importFilePath}}?antInclude=*.hl7&preMove=stage&move=completed")
      .convertBodyTo(String.class)
      .marshal().hl7()
      .to("bean:BundleProcessor")
      .marshal().fhirJson()
      .log("Sending to Fhir")
      .to("fhir://transaction/withBundle?inBody=stringBundle" 
          + "&serverUrl={{fhirServer}}"
          + "&fhirVersion={{fhirVersion}}"
          + "&validationMode=NEVER"
          + "&log=true")
        .log("Message Sent")
        .end();
        
  }
}