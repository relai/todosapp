package sample.todosapp.vertx;

import com.github.relai.vertx.springdata.AsyncCrudRepository;
import com.github.relai.vertx.springdata.MessageHandler;
import org.vertx.java.core.json.JsonArray;


/**
 * Asynchronous client API for TodoRepository, enabling asynchronous 
 * CRUD operations on the Todo entity using mod-spring-data
 * 
 * @author relai
 */
 public interface AsyncTodoRepository extends AsyncCrudRepository<Long>{
     void findByCompleted(Boolean completed, MessageHandler<JsonArray> onreply);
}
