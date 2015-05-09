/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.todosapp.vertx.simplified;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import sample.todosapp.vertx.domain.Config;
import sample.todosapp.vertx.domain.Todo;
import sample.todosapp.vertx.domain.TodoRepository;

/**
 *
 * @author relai
 */
public class TodoDataVerticle extends Verticle{
    
    private ConfigurableApplicationContext springContext;  
    
	@Override 
	public void start() {   
        // Initialize the verticle
        SpringApplication app = new SpringApplication(Config.class);
        springContext = app.run();
        
        vertx.eventBus().registerHandler("TodoData", this::onMessage);
    }
    
     @Override 
    public void stop() {
        super.stop();
        if (springContext != null) {
            springContext.close();
        }
    }
    
    private void onMessage(Message<JsonObject> message) {
        JsonObject command = message.body();   
        
        // Only "findOne" is supported in this example 
        assert ("findOne".equals(command.getString("action"))); 
        
        Long id = command.getLong("id");
        Todo todo = findOneById(id);
        JsonObject payload = toJson(todo);
        message.reply(payload);
    }
    
    private Todo findOneById(Long id) {
        TodoRepository repository = springContext.getBean(TodoRepository.class);
        return repository.findOne(id);     
    }
  
    
    private JsonObject toJson(Object pojo){
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(pojo, HashMap.class);
        return new JsonObject(map);
    }
    
}
