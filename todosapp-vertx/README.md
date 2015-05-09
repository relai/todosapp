todosapp in Vert.x
===================

Implements todosapp using the server-side asynchronous processing in [Vert.x](http://vertx.io/)


####Homepage
  http://localhost:8080

Three "applications" are present in the project:

####Web Application
The application is rendered at the server side
- URL: `http://localhost:8080/todos`
- Technology: [Yoke](http://pmlopes.github.io/yoke/), [vert-spring-data](https://github.com/relai/vertx-spring-data)
- UI template: server-side handlebars

####RESTful Web Service
- URL: `http://localhost:8080/api/todos`
- Technology: [Yoke](http://pmlopes.github.io/yoke/), [vertx-spring-data](https://github.com/relai/vertx-spring-data)

####Single Ppage Application (spa)
A rich Internet client rendered at the browser side
- URL: `http://localhost:8080/spa`
- Technology: Backbone.js on top of the  RESTful web service 
- UI template: handlebars (at the browser side)

###ORM

The ORM uses Hibernate (JPA), running on top of an in-memory HSQLDB.

###Maven 

The project uses Maven as the build system. To build the application, run Maven command as follows

    mvn clean install

To run the application, use Maven command as follows:

     mvn vertx:runMod
     
This deploys and runs the application on an embedded Tomcat.


