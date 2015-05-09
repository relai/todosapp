package controllers;

import models.TodoItem;
import models.TodoService;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * To-do REST web service controller.
 *
 * This is a very simple sample code. Little effort was made for error handling.
 *
 * @author Re Lai
 */
public class TodoRestApp extends Controller {

    @Transactional(readOnly = true)
    public static Result findAll() {
        return ok(toJson(TodoService.findAll()));
    }

    @Transactional(readOnly = true)
    public static Result findOne(Long id) {
        return ok(toJson(TodoService.findOne(id)));
    }

    @Transactional
    public static Result create() {
        TodoItem todo = fromJson(request().body().asJson(), TodoItem.class);
        TodoItem result = TodoService.create(todo);
        return created(toJson(result));
    }

    @Transactional
    public static Result update(Long id) {
        TodoItem todo = fromJson(request().body().asJson(), TodoItem.class);
        TodoItem result = TodoService.update(todo);
        return ok(toJson(result));
    }

    @Transactional
    public static Result delete(Long id) {
        TodoService.delete(id);
        return noContent();
    }

}
