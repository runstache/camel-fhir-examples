package com.lswebworld.fhir.camelfhirexamples.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "oid")
public class OidSettings {

  private String patientOid;
  private String encounterOid;
  private String locationOid;

  public String getPatientOid() {
    return patientOid;
  }

  public String getEncounterOid() {
    return encounterOid;
  }

  public String getLocationOid() {
    return locationOid;
  }

  public void setPatientOid(final String value) {
    patientOid = value;
  }

  public void setEncounterOid(final String value) {
    encounterOid = value;
  }

  public void setLocationOid(final String value) {
    locationOid = value;
  }
}