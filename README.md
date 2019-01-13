# Camel Fhir Component Examples #

This is a project that shows examples of using the Camel Fhir Component.  The goal is to put out some simple examples to show how the component can be used with Apache Camel.  Each example will be created as a Route in the application. The routes should be able to run without interfering with each other.  I will work to add new examples as time permits.

## Disclaimer ##

These examples are as is and do not provide any sort of production support.  My goal in this project is to just provide something for other developers to serve as a sort of jumping off without the need to dig into the code of the component to figure out how to use it.  There are very little unit tests as these are really just some examples on utilizing the component.

### Examples Included ###

The following examples are included within the project

* __Bundle:__ An implementation of the Camel Fhir Component that creates and sends Fhir Bundle containing a patient, encounter and locations from a HL7 message.

### Extras ###

I have also included a Docker Compose file that will create an instance of the Hapi Fhir Example JPA Server with a MySQL Database backend.  You can execute the `docker-compose up -d` to start the server.

### Settings ###

The following settings in application.settings are used by all of the Examples:

* __fhirServer:__ This is the URL to the Fhir Server. By default this is set to the url of the contain Fhir Server.
* __fhirVersion:__ This is the version of Fhir. The current examples utilize the DSTU3 objects.
* __logging.file:__ This is the location where the logfile will be written to.
* __logging.level.root:__ This is the default logging level.
* __camel.springboot.name:__ This is the Camel Context Name that is created in Spring Boot.
* __camel.springboot.main-run-controller:__ This option will start the Camel Routes and keep them running. This prevents Springboot from shutting down Camel after initializing.