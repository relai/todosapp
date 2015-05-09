package sample.todosapp.spring.controller;

import java.util.concurrent.Callable;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import sample.todosapp.spring.domain.Todo;
import sample.todosapp.spring.service.TodoRepository;
import static sample.todosapp.error.ErrorMessages.*;

/**
 * To-do web application, rendering JSP page asynchronously using Spring MVC
 * by Spring MVC.
 *
 * @author relai
 */
@Controller
@RequestMapping("/todos")
public class TodoWebAppController {
    
    /** The application service for CRUD operations on Todo entities */
    @Autowired TodoRepository todoRepository;

    /**
     * Get the to-do list, filtered by the completion status (all, open and
     * done).
     *
     * @param filter filtering to-items based on the completion status: all,open and done.
     * @param model
     * @return
     */
    @RequestMapping(method = GET)
    public Callable<String>
        getAllTodos(@RequestParam(value = "filter", defaultValue = "all") String filter,
            Model model) {
        return () -> doGetAllTodos(filter, model);
    }

    /**
     * Get the to-do form, for either creation or editing an existing item. The
     * sub-path is either /create or /{id}.
     */
    @RequestMapping(value = "/{subitem}", method = GET)
    public Callable<String> getTodo(@PathVariable("subitem") String subitem, Model model) {
        return () -> doGetTodo(subitem, model);
    }

    /**
     * Delete a to-do item.
     */
    @RequestMapping(value = "/{id}", params = "action=delete", method = POST)
    public Callable<String> deleteTodo(@PathVariable("id") Long id) {
        return () -> doDeleteTodo(id);
    }

    /**
     * Creates a to-do item.
     */
    @RequestMapping(value = {"/create", "/{id}"}, params = "action=save", method = POST)
    public Callable<String> saveTodo(@Valid @ModelAttribute Todo todo,
        BindingResult bindingResult, Model model) {
        return () -> doSaveTodo(todo, bindingResult, model);
    }

    /**
     * Error handler for optimistic locking failure.
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public String onConflict(HttpServletRequest req, Exception ex) {
        return handleError(req, ex, CONFLICT_MSG);
    }
    
    /**
     * Error handler for not-found.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public String onNotFound(HttpServletRequest req, Exception ex) {
        return handleError(req, ex, NOT_FOUND_MSG);
    }
    
    /**
     * Fall-back error handler for all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public String onError(HttpServletRequest req, Exception ex) {
        return handleError(req, ex, "Something went wrong.");
    }

    private String doGetAllTodos(String filter, Model model) {
        Iterable<Todo> todos;
        switch (filter) {
            case "open":
                todos = todoRepository.findByCompleted(false);
                break;
            case "done":
                todos = todoRepository.findByCompleted(true);
                break;
            case "all":
            default:
                todos = todoRepository.findAll();
                filter = "all";
        }
        model.addAttribute("todos", todos)
             .addAttribute("filter", filter);
        return "todo-list";
    }

    private String doGetTodo(String subitem, Model model) {
        Todo todo;
        if ("create".equals(subitem)) {
            todo = new Todo();
        } else {
            Long id = Long.valueOf(subitem);
            todo = todoRepository.findOne(id);
            if (todo == null) {
                throw new EntityNotFoundException();
            }
        }
        model.addAttribute(todo);
        return "todo-form";
    }

    private String doDeleteTodo(@PathVariable("id") Long id) {
        todoRepository.delete(id);
        return "redirect:/todos";
    }

    private String doSaveTodo(Todo todo, BindingResult bindingResult,
        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(todo);
            return "todo-form";
        }
        todoRepository.save(todo);
        return "redirect:/todos";
    }
    
    private String handleError(HttpServletRequest req, Exception ex,
        String message) {
        req.setAttribute("exception", ex);
        req.setAttribute("url", req.getRequestURL());
        req.setAttribute("message", message);
        return "error-page";
    }
}
