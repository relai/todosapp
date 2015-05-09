package sample.todosapp.vertx;

/**
 * The to-do application in Vert.x, including both the web application and 
 * the REST web service.
 *
 * @author relai
 */

import org.vertx.java.core.Future;
import org.vertx.java.platform.Verticle;

import com.jetdrone.vertx.yoke.*;
import com.jetdrone.vertx.yoke.engine.HandlebarsEngine;
import com.jetdrone.vertx.yoke.middleware.*;
import com.github.relai.vertx.springdata.*;

import sample.todosapp.vertx.domain.Config;
import sample.todosapp.vertx.extension.handlebars.IfEqualHelper;
import sample.todosapp.vertx.extension.jar.JarResources;

public class TodoApp extends Verticle {   

    @Override
    public void start(Future<Void> startedResult) {                    
  
        Router router = new Router();
        
        //
        // Routes home pages "/" and "/spa" to webjars resources
        //
        JarResources resources = new JarResources();
        router.get("/",    resources.send("/webjars/todosapp/index.html"))
              .get("/spa", resources.send("/webjars/todosapp/backbone-spa/spa.html"));
        
        //
        //Set up the server-based web app
        //
        AsyncRepositoryBuilder<Long> builder
            = new AsyncRepositoryBuilder<>(vertx.eventBus());  
        AsyncTodoRepository repository = builder.build(AsyncTodoRepository.class);

        TodoAppHandler webApp = new TodoAppHandler(repository);
        router.get("/todos",         webApp::getTodos)
              .get("/todos/create",  webApp::createTodo)
              .post("/todos/create", webApp::postTodo)
              .get("/todos/:id",     webApp::eidtTodo)
              .post("/todos/:id",    webApp::postTodo);
        
        // The server-side template engine middlware
        HandlebarsEngine handlebars = new HandlebarsEngine("handlebars");
        handlebars.registerHelper("ifEqual", new IfEqualHelper());    
        
        //
        // Set up the RESTful web service 
        //
         YokeRestHelper<Long> rest = new YokeRestHelper<>(repository, Long.class);  
         
        //
        // Start Yoke
        //
        Yoke yoke = new Yoke(vertx);      
        yoke.use(new BodyParser())
            .use("/webjars", resources) // serving static contents from webjars
            .engine(handlebars)
            .use(router)  // router for the web application
            .use(rest.createRouter("/api/todos")) //router for the REST web service
            .listen(8080);
        
        //
        // Deploy Srping Data mod
        //
        SpringDeployer deployer = new SpringDeployer(container);
        deployer.springConfigClass(Config.class)
                .deploy(it -> startedResult.setResult(null));
    }  
}
