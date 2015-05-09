todosapp in Spring MVC
===================

Implements todosapp using the server-side asynchronous processing in Spring MVC


###Applications

*Homepage*
  
    http://localhost:8080

Three applications are present in the project.

*Web application* rendered at the server side
-URL: `http://localhost:8080/todos`
-Technology: asynchronous Spring MVC web controller, Spring Data, Spring Boot
-UI template: JSP

*RESTful Web Service*
-URL: `http://localhost:8080/api/todos`
-Technology: asynchronous Spring MVC REST controller, Spring Data, Spring Boot

*Single-page application*: a rich Internet client rendered at the browser side
-URL: `http://localhost:8080/spa`
-Technology: Backbone.js on top of the aforementioned RESTful web service 
-UI template: handlebars (at the browser side)

###ORM

The ORM uses Hibernate (JPA), running on top of an in-memory HSQLDB.

###Maven 

The project uses Maven as the build system. 

To build the application, run Maven command as follows
    mvn clean install

To run the application, use Maven command as follows:
     mvn spring-boot:run 
This launches an embedded Tomcat and deploys the application on it. The web application can then be accessed in a web browser at
    http://localhost:8080

To enable remote debugging of the application, 
   mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"

###Special Note NetBeans Integration

If you open the module as a Maven project in NetBeans, you can run NetBeans command Clean, Build, Run and Debug, leveraging NetBeans integration with Maven.

Note that when you run Debug, it only runs the application with remote debugging enabled. Afterwards, you need to attach NetBeans by remote debuggin at port 8000.

