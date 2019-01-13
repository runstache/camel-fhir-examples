# Fhir Bundle Example #

This example shows how to create a simple Fhir Bundle containing a Patient, Encounter, and Locations from a sample HL7 message and send the Bundle to a Hapi Fhir Server.
This Example utilizes the BundleRouteBuilder within the project.

## Settings ##

This example uses the following settings in the application.properties file:

* importFilePath: This is the location when the HL7 files are picked up from.
* oid.locationOid: This is the system value used for Fhir Location Identifiers.
* oid.encounterOid: This is the system value used for Fhir Encounter Identifiers.
* oid.patientOid: This is the system value used for Fhir Patient Identifiers.

### Running the Applications ###

This application can be run utilizing Maven.  Once running, you can place the adt.hl7 file that is in the samples directory in your import file path. The Camel Route will pick up this file and convert it to the following Fhir components and send them to the Fhir Server as a Fhir Bundle:

* Fhir Patient
* Fhir Encounter
* Fhir Location (Building)
* Fhir Location (Room)
* Fhir Location (Bed)

All Mapping for this example have been completed based on the sample ADT HL7 Message.  These are done within the FhirUtil Class.
