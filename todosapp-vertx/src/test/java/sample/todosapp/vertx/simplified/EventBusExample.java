package sample.todosapp.vertx.simplified;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;
import static org.vertx.testtools.VertxAssert.testComplete;

/**
 *
 * @author relai
 */
public class EventBusExample extends TestVerticle{
    
    
    @Override
    public void start() {
        initialize();

        getContainer().deployModule(System.getProperty("vertx.modulename"), result -> {
            assertTrue(result.succeeded());
            assertNotNull("deploymentID should not be null", result.result());                                  
            startTests();
        });
    }
    
    @Test
    public void home() {
        createHttpClient().get("/todos", resp -> {
            assertEquals(200, resp.statusCode());
            resp.bodyHandler(( Buffer data) -> {
                String page = data.toString();
                assertTrue(page.indexOf("todosapp") > 0);
                assertTrue(page.indexOf("Buy coffee") > 0);
                testComplete();
            });
        }).end();
    }
    
    private HttpClient createHttpClient() {
        return getVertx().createHttpClient()
                         .setHost("localhost")
                         .setPort(8080);
    }
    
    //@Test 
    public void eventBusExample() {
        String address = "com.github.relai.vertx.springdata.RepositoryVerticle";
        
        JsonObject command = new JsonObject();
        command.putString("action", "findOne")
               .putNumber("id", 1);

        Handler<Message<JsonObject>> handler = (message) -> {
          JsonObject todo = message.body();  
          container.logger().info(todo);
        };
        
        vertx.eventBus().send(address, command, handler);                        
    }
}
