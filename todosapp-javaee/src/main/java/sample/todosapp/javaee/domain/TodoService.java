package sample.todosapp.javaee.domain;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.EntityNotFoundException;

/**
 * The application service for CRUD operations on to-do items. It is a thin 
 * wrapper on top of JPA entity manager.
 *
 * @author relai
 */
@Stateless
public class TodoService {

    @PersistenceContext private EntityManager em;

    public List<Todo> findAll() {
        TypedQuery<Todo> q = em.createNamedQuery("Todo.findAll", Todo.class);
        return q.getResultList();
    }

    public List<Todo> findByCompleted(boolean isDone) {
        TypedQuery<Todo> q = em.createNamedQuery("Todo.findByCompleted", Todo.class);
        q.setParameter("done", isDone);
        return q.getResultList();
    }

    public Todo create(Todo item) {
        em.persist(item);
        return item;
    }

    public Todo findOne(Long id) {
        Todo item = em.find(Todo.class, id);
        if (item == null) {
            throw new EntityNotFoundException();
        }
        return item;
    }

    public Todo update(Todo item) {
        return em.merge(item);
    }

    public void delete(Long id) {
        Todo item = findOne(id);
        em.remove(item);
    }
}
