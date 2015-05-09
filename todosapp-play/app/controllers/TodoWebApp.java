package controllers;

import java.util.List;
import java.util.Map;

import play.mvc.*;
import play.data.Form;
import play.db.jpa.Transactional;
import views.html.*;
import models.TodoItem;
import models.TodoService;

/**
 * To-do web application controller.
 *
 * This is a very simple sample code. Little effort was made for error handling.
 * 
 * @author relai
 */
public class TodoWebApp extends Controller {

    private final static Form<TodoItem> form = Form.form(TodoItem.class);

    /**
     * Gets the to-do list, filtered by the completion status (all, open and
     * done).
     *
     * @param filter filtering to-items based on the completion status: all,
     * open and done.
     * @return
     */
    @Transactional(readOnly = true)
    public static Result list(String filter) {
        List<TodoItem> todos;
        switch (filter) {
            case "open":
            case "done":
                todos = TodoService.findByCompleted("done".equals(filter));
                break;
            case "all":
            default:
                todos = TodoService.findAll();
        }
        return ok(todolist.render(filter, todos));
    }

    /**
     * Gets the to-do form.
     *
     * @param id the to-to item item
     * @param isInserting whether the form is to create a new to-do item or to
     * edit an existing one.
     * @return
     */
    //The following block executes the action workload asynchronously in Akka 
    //
    // public static Promise<Result> form(Long id) {		
    //	return Promise.promise(()-> TodoService.findOne(id))		
    //	              .map(todo -> ok(todoform.render(todo)));
    //}
    @Transactional(readOnly = true)
    public static Result form(Long id) {
        TodoItem item = TodoService.findOne(id);
        return ok(todoform.render(item));
    }

    /**
     * Processes the POST action from the to-do item form.
     *
     * @param path unused in the method
     * @return
     */
    @Transactional
    public static Result postForm(String path) {
        String action = "";
        Map<String, String[]> formEncoded = request().body().asFormUrlEncoded();
        if (formEncoded != null) {
            String[] value = formEncoded.get("action");
            if (value != null && value.length > 0) {
                action = value[0];
            }
        }
        switch (action) {
            case "save":
                TodoItem todo = form.bindFromRequest().get();
                TodoService.save(todo);
                break;
            case "delete":
                Long id = Long.valueOf(path);
                TodoService.delete(id);
                break;
        }
        return redirect(routes.TodoWebApp.list("all"));
    }

}
