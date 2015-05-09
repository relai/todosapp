package sample.todosapp.vertx.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring repository class for Todo, providing CRUD operations on to-do items.
 * 
 * @author Re Lai
 */
public interface TodoRepository extends CrudRepository<Todo,Long> {
	List<Todo> findByCompleted(boolean completed);
}
