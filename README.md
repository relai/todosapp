todosapp 
========

`todosapp` is a sample application that manages your to-do items. It is built primarily to explore the server-side asynchronous programming in JVM web frameworks.
It is inspired by [todomvc](http://todomvc.com/), showcasing MV* JavaScript frameworks at the browser side.

### JVM Web Frameworks
In the sub-projects, we build the sample project using the following frameworks respectively:
- Java EE: Servlet and JAX-RS
- Spring MVC, with Spring Data and Spring Boot
- Vert.x 2.0 for Java
- Play 2 Framework for Java


### Applications

Once running, the web application can be accessed at its **homepage**
    
     http://localhost:8080
 
There are actually three "applications" packaged in each sub-project:

- **Web application** rendered at the server side:

    http://localhost:8080/todos


- **RESTful Web Service** providing CRUD operations of to-do items:

    http://localhost:8080/api/todos


- **Single-page application**: a rich Internet client rendered at the browser side:

    http://localhost:8080/spa
   
    This is a Backbone.js application backed by the RESTful web service at `api/todos`.


### Java 8

All modules are implemented in Java 8, taking advantage of the Lambda expressions.


###Maven Integration

The build system is Maven. Maven 3.2.2 or newer should be used. 
Older versions of Maven can result in CDI linkage errors.

To build all sub-projects, run

    mvn clean install

Each application can be deployed and run on its embedded application server with an in-memory database directly from Maven. 
Refer to the instruction in each project for how to run the application.

### NetBeans Integration

If you open the module as a Maven project in NetBeans, you can leverage NetBeans integration with Maven to run NetBeans command Clean, Build, Run and Debug in each sub-project.

