todosapp in Play 2 Framework for Java
======================================

Implements todosapp using the server-side asynchronous processing in [Play](https://www.playframework.com/).

The sample application was developed in
   - Play Framework: 2.3.8
   - Scala: 2.11.1

####Homepage
    http://localhost:8080

There are actually three "applications" packaged in the project:

####Web Application
The application is rendered at the server side.
- URL: `http://localhost:8080/todos`
- Technology: Play 2 
- UI template: Scala templates

####RESTful Web Service
- URL: `http://localhost:8080/api/todos`
- Technology: Play 2

####Single Page Application
A rich Internet client application rendered at the browser side.
- URL: `http://localhost:8080/spa`
- Technology: Backbone.js on top of the above REST web service 
- UI template: handlebars (at the browser side)

###ORM

The ORM uses Hibernate (JPA), running on top of an in-memory H2 database.

###Build

The build sytem is  [activator](https://www.playframework.com/documentation/2.3.x/PlayConsole). 

Briefly,
- Clean: `activator clean` 
- Run: `activator run 8080`
- Run with remote debuging enabled at port 9999: `activator -jvm-debug run 8080` 

###Maven Integration

Integration of  build with Maven is achieved with the help of exec-maven-plugin.

To build the project 

    mvn clean install

To run the application 

     mvn exec:exec -Dexec.executable=activator.bat -Dexec.args="run 8080"

###Special Note on NetBeans Integration

If you open the module as a Maven project in NetBeans, you can run NetBeans command Clean, Build, Run and Debug, leveraging NetBeans integration with Maven.

Note that when you run Debug in this module, it only runs the application with remote debugging enabled. Afterwards, you need to attach NetBeans by remote debuggin at port 9999.



