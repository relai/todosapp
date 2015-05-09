todosapp in Java EE
===================

Implementing todosapp in Java EE using the server-side asynchronous processing.


###Applications

The web application can be accessed in a web browser at its **homepage** 

      http://localhost:8080
      
To change the port number, modify pom.xml.

There are three "applications" packaged together in the project.

####Web application
The application is rendered at the server side:

- URL: `http://localhost:8080/todos`
- Technology: *asynchronous* Servlet 
- UI template: JSP

####Rest Web Service
- URL: `http://localhost:8080/api/todos`
- Technology: asynchronous Jersey (JAX-RS) 

####Single-page application (spa)
A rich Internet client rendered at the browser side.

- URL: `http://localhost:8080/spa`
- Technology: Backbone.js on top of the Rest web service 
- UI template: handlebars (at the browser side)

###ORM

The object relationship mapping (ORM) uses EclipseLink (JPA), running on top of an embedded Java DB.  

###Maven 

The project is built as a Maven project. To run the application, run Maven command as follows:

    mvn clean package embedded-glassfish:run

The goal `embedded-glassfish:run` launches the application on an embedded glassfish server along with the embedded Java DB.

This project depends on module `todosapp-sharedresources`, which must be installed first.
