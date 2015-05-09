/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.todosapp.vertx.simplified;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import static org.vertx.testtools.VertxAssert.testComplete;

/**
 *
 * @author relai
 */
public class TodoTestApp extends TestVerticle{
    
    @Override
    public void start() {
        initialize();
        
        RouteMatcher matcher = new RouteMatcher();
        matcher.get("/todos/:id", this::getOne);
        
        vertx.createHttpServer()
             .requestHandler(matcher)
             .listen(8080, "localhost");
        
        final String verticleName = "sample.todosapp.vertx.simplified.TodoDataVerticle";
        container.deployVerticle(verticleName, result -> {
            assertTrue(result.succeeded());
            assertNotNull("deploymentID should not be null", result.result());                                  
            startTests();
        });
    }
    
    private void getOne(HttpServerRequest request) {
        String address = "TodoData";
        
        JsonObject command = new JsonObject();
        command.putString("action", "findOne")
              .putNumber("id", Long.valueOf(request.params().get("id")));
        
        Handler<Message<JsonObject>> onreply = (message)-> {
            JsonObject todo = message.body();
            String payload = todo.encode();
            request.response()
	    		   .putHeader("content-type", "application/json")
	    	       .end(payload);
        };
        
        vertx.eventBus().send(address, command, onreply);
    }
    
     
    @Test
    public void testGetOne() {
        createHttpClient().get("/todos/1", resp -> {
            assertEquals(200, resp.statusCode());
            resp.bodyHandler(( Buffer data) -> {
                String payload = data.toString();
                container.logger().info(payload);
                assertTrue(payload.indexOf("Buy coffee") > 0);
                testComplete();
            });
        }).end();
        
    }
    
    private HttpClient createHttpClient() {
        return getVertx().createHttpClient()
                         .setHost("localhost")
                         .setPort(8080);
    }
}
      
