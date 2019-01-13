package com.lswebworld.fhir.camelfhirexamples.processors;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import com.lswebworld.fhir.camelfhirexamples.util.FhirUtil;
import java.util.UUID;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Camel Processor that will ingest an HL7 Message and convert it to
 * a simple Fhir Bundle containing a Patient, Encounter and Locations.
 */
@Component
public class BundleProcessor implements Processor {

  @Autowired
  private FhirUtil util;

  @Override
  public void process(final Exchange exchange) throws HL7Exception {

    //Check for an Empty Message Body.
    if (exchange.getIn().getBody() != null) {
      final Message msg = exchange.getIn().getBody(Message.class);

      //Create Guids for each component
      final UUID encounterGuid = UUID.randomUUID();
      final UUID patientGuid = UUID.randomUUID();
      final UUID buildingGuid = UUID.randomUUID();
      final UUID roomGuid = UUID.randomUUID();
      final UUID bedGuid = UUID.randomUUID();
      
      //Build the Fhir Components
      final Patient patient = util.createPatient(msg);
      final Location building = util.createBuilding(msg);
      final Location room = util.createRoom(msg);
      final Location bed = util.createBed(msg);
      final Encounter encounter = util.createEncounter(msg);

      //Link up the locations.
      room.setPartOf(util.createReference(buildingGuid.toString()));
      bed.setPartOf(util.createReference(roomGuid.toString()));
      
      //Add the patient to the Encounter
      encounter.setSubject(util.createReference(patientGuid.toString()));

      //Add the Locations to the Encounter
      encounter.addLocation(util.createEncounterLocation(buildingGuid));
      encounter.addLocation(util.createEncounterLocation(roomGuid));
      encounter.addLocation(util.createEncounterLocation(bedGuid));

      //Build the Bundle
      final Bundle bundle = new Bundle();
      bundle.setType(BundleType.TRANSACTION);
      bundle.addEntry(util.createBundleEntry(patient, patientGuid, HTTPVerb.POST));

      /**
       * I'm using PUT for the Encounter since I've added 
       * the Identifier into the Bundle Component URL.
       * This will cause the item to do an update and place 
       * a new Version of the encounter in the Fhir Server.
       */         
      bundle.addEntry(util.createBundleEntry(encounter, encounterGuid, HTTPVerb.PUT));
      
      bundle.addEntry(util.createBundleEntry(building, buildingGuid, HTTPVerb.POST));
      bundle.addEntry(util.createBundleEntry(room, roomGuid, HTTPVerb.POST));
      bundle.addEntry(util.createBundleEntry(bed, bedGuid, HTTPVerb.POST));

      //Add the Bundle the Message Body
      exchange.getIn().setBody(bundle);
    }
  }
}