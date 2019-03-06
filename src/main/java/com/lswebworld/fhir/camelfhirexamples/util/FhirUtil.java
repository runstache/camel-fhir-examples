package com.lswebworld.fhir.camelfhirexamples.util;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

import ca.uhn.hl7v2.util.Terser;

import com.lswebworld.fhir.camelfhirexamples.configuration.OidSettings;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus;
import org.hl7.fhir.dstu3.model.Encounter.EncounterStatus;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirUtil {

  @Autowired
  private OidSettings settings;

  /**
   * Creates a new Fhir Patient from an HL7 Message.
   * 
   * @param hl7Message HL7 Message
   * @return Fhir Patient Resource
   * @throws HL7Exception HL7 Exception.
   */
  public Patient createPatient(Message hl7Message) throws HL7Exception {

    final Patient patient = new Patient();

    // Add the Patient Identifier
    patient.addIdentifier(buildIdentifier(settings.getPatientOid(), 
        getHl7Value("PID-3-1", hl7Message), true));

    // Add the Patient Name
    final HumanName name = new HumanName();
    name.setFamily(getHl7Value("PID-5-1", hl7Message));
    name.setText(getHl7Value("PID-5-2", hl7Message));
    name.setUse(NameUse.USUAL);
    patient.addName(name);

    // Add the Patient's Address
    final Address address = new Address();
    address.addLine(getHl7Value("PID-11-1", hl7Message));
    address.setCity(getHl7Value("PID-11-3", hl7Message));
    address.setState(getHl7Value("PID-11-4", hl7Message));
    address.setPostalCode(getHl7Value("PID-11-5", hl7Message));
    address.setUse(AddressUse.HOME);
    address.setType(AddressType.BOTH);
    patient.addAddress(address);

    return patient;
  }

  /**
   * Creates a Simple Fhir Encounter from an HL7 Object.
   * 
   * @param hl7Message HL7 Message
   * @return Fhir Encounter
   * @throws HL7Exception HL7 Exception.
   */
  public Encounter createEncounter(final Message hl7Message) throws HL7Exception {
    final Encounter encounter = new Encounter();
    encounter.addIdentifier(buildIdentifier(
        settings.getEncounterOid(), getHl7Value("PV1-19-1", hl7Message), false));
    Period period = new Period();
    period.setStart(toDate(getHl7Value("PV1-44-1", hl7Message)));

    if (getHl7Value("MSH-9-2", hl7Message).equals("A04")) {
      encounter.setStatus(EncounterStatus.FINISHED);
      period.setEnd(toDate(getHl7Value("PV1-45-1", hl7Message)));
    } else {
      encounter.setStatus(EncounterStatus.INPROGRESS);
    }
    encounter.setPeriod(period);
    
    return encounter;
  }

  /**
   * Creates a Simple Fhir Location for the Facility.
   * 
   * @param hl7Message Hl7 Message.
   * @return Fhir Location.
   */
  public Location createBuilding(final Message hl7Message) throws HL7Exception {
    final Location building = new Location();
    building.setPhysicalType(buildPhysicalType("bu", "Building"));
    building.addIdentifier(buildIdentifier(
          settings.getLocationOid(), getHl7Value("PV1-3-4-1", hl7Message), false));
    building.setDescription(getHl7Value("/.ZPD(0)-2-1-1",hl7Message));
    return building;
  }

  /**
   * Creates a Simple Fhir Location for the Room.
   * 
   * @param hl7Message Hl7 Message.
   * @return Fhir Location.
   * @throws HL7Exception Hl7 Exception.
   */
  public Location createRoom(final Message hl7Message) throws HL7Exception {
    final Location room = new Location();
    room.addIdentifier(buildIdentifier(settings.getLocationOid(),
        getHl7Value("PV1-3-4-1", hl7Message) + "^" + getHl7Value("PV1-3-2", hl7Message), false));
    room.setPhysicalType(buildPhysicalType("ro", "Room"));
    return room;
  }

  /**
   * Creates a simple Fhir Location for the Bed.
   * 
   * @param hl7Message Hl7 Message
   * @return Fhir Location.
   * @throws HL7Exception Hl7 Exception
   */
  public Location createBed(final Message hl7Message) throws HL7Exception {
    final Location bed = new Location();
    bed.addIdentifier(buildIdentifier(
        settings.getLocationOid(), getHl7Value("PV1-3-4-1", hl7Message) + "^"
        + getHl7Value("PV1-3-2", hl7Message) + "^" + getHl7Value("PV1-3-3", hl7Message), false));
    bed.setPhysicalType(buildPhysicalType("bd", "Bed"));
    return bed;
  }

  /**
   * Creates a Encounter Location Component based on a Guid.
   * 
   * @param guid Location Guid.
   * @return Encounter Location Component.
   */
  public EncounterLocationComponent createEncounterLocation(final UUID guid) {
    EncounterLocationComponent component = 
        new EncounterLocationComponent(createReference(guid.toString()));
    component.setStatus(EncounterLocationStatus.ACTIVE);
    return component;
  }

  /**
   * Creates a Fhir Bundle Entry Component for a Patient.
   * 
   * @param patient Patient.
   * @param guid    Patient Guid.
   * @param method  Http Method.
   * @return Fhir Bundle Component.
   */
  public BundleEntryComponent createBundleEntry(final Patient patient, 
      final UUID guid, final HTTPVerb method) {

    final BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(patient);
    component.setFullUrl("urn:uuid:" + guid.toString());

    final BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(method);
    request.setUrl("Patient");
    request.setIfNoneExist(
        "identifier=" 
        + patient.getIdentifierFirstRep().getSystem() 
        + "|" 
        + patient.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for an Encounter.
   * 
   * @param encounter Fhir Encounter
   * @param guid      Encounter Guid
   * @param method    HTTP Verb.
   * @return Bundle Entry.
   */
  public BundleEntryComponent createBundleEntry(final Encounter encounter, 
      final UUID guid, final HTTPVerb method) {
    final BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(encounter);
    component.setFullUrl("urn:uuid:" + guid.toString());

    final BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(method);
    request.setUrl("Encounter?identifier=" 
        + encounter.getIdentifierFirstRep().getSystem() 
        + "|"
        + encounter.getIdentifierFirstRep().getValue());

    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for a Fhir Location.
   * @param location Fhir Location
   * @param guid Guid
   * @param method HTTP Verb
   * @return Bundle Entry
   */
  public BundleEntryComponent createBundleEntry(final Location location, 
      final UUID guid, final HTTPVerb method) {

    final BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(location);
    component.setFullUrl("urn:uuid:" + guid.toString());

    final BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(method);
    request.setUrl("Location");
    request.setIfNoneExist("identifier=" + location.getIdentifierFirstRep().getSystem() + "|"
        + location.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  public Reference createReference(final String guid) {
    return new Reference("urn:uuid:" + guid);
  }

  //Private Methods for creating the Fhir Components for simplicity.
  private String getHl7Value(final String path, final Message hl7Message) throws HL7Exception {
    final Terser terser = new Terser(hl7Message);
    return terser.get(path);

  }

  private Identifier buildIdentifier(final String system, final String value, final boolean isMrn) {
    final Identifier id = new Identifier();

    id.setSystem(system);
    id.setValue(value);
    id.setUse(IdentifierUse.USUAL);

    if (isMrn) {
      final Coding coding = new Coding();
      coding.setCode("MR");
      coding.setSystem("http://hl7.org/fhir/ValueSet/identifier-type");
      coding.setDisplay("Medical record number");

      final CodeableConcept typeCode = new CodeableConcept();
      typeCode.addCoding(coding);
      id.setType(typeCode);
    }

    return id;

  }

  private Date toDate(final String dateString) {
    final Calendar cal = new Calendar.Builder()
        .setDate(
          Integer.parseInt(dateString.substring(0, 4)),
          Integer.parseInt(dateString.substring(4, 6)),
          Integer.parseInt(dateString.substring(6, 8)))
        .setTimeOfDay(
          Integer.parseInt(dateString.substring(8, 10)), 
          Integer.parseInt(dateString.substring(10, 12)), 
          0).build();
    return new Date(cal.getTimeInMillis());

  }



  private CodeableConcept buildPhysicalType(final String code, final String display) {
    final Coding coding = new Coding();
    coding.setCode(code);
    coding.setDisplay(display);
    coding.setSystem("http://hl7.org/fhir/location-physical-type");

    final CodeableConcept physicalType = new CodeableConcept();
    physicalType.addCoding(coding);

    return physicalType;
  }
}