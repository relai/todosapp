package models;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import play.db.jpa.JPA;

/**
 * The application service for CRUD operations on  TodoItem entities.
 * 
 * @author relai
 */

public class TodoService {

    public static List<TodoItem> findAll() {
        TypedQuery<TodoItem> q = JPA.em().createNamedQuery("Todo.findAll", TodoItem.class);
	    return q.getResultList();
    }
    
	public static List<TodoItem> findByCompleted(boolean done) { 
        TypedQuery<TodoItem> q = JPA.em().createNamedQuery("Todo.findByCompleted", TodoItem.class);
        q.setParameter("done", done);
        return q.getResultList();
    }
    
    public static TodoItem create(TodoItem todo) {
    	JPA.em().persist(todo);
        return todo;
    }
    
    public static TodoItem findOne(Long id) {
        TodoItem item =  (id == 0L)? new TodoItem() :
            JPA.em().find(TodoItem.class, id);	
        if (item == null) {
            throw new EntityNotFoundException();
        }
        return item;
    }
    
	public static TodoItem update(TodoItem todo) {
        TodoItem entity = JPA.em().merge(todo);
        return entity;
    }
	
	public static TodoItem save(TodoItem todo) {
		TodoItem result;
		if (todo.getId() == null || todo.getId() == 0L) {
			result = create(todo);
		} else {
			result = update(todo);
		}
        return result;
    }
    
	public static void delete(Long id){
        TodoItem entity = findOne(id);
        JPA.em().remove(entity);
    }
}
