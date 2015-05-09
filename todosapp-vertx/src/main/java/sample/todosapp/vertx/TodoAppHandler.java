package sample.todosapp.vertx;

import static io.netty.handler.codec.http.HttpResponseStatus.CONFLICT;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_ACCEPTABLE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.eventbus.ReplyException;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import com.github.relai.vertx.springdata.MessageHandler;
import com.jetdrone.vertx.yoke.middleware.YokeRequest;

/**
 * Todosapp web application handlers.
 * 
 * @author relai
 */
class TodoAppHandler {
    
    // form attributes
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static String PRIORITY = "priority";
    private final static String COMPLETED = "completed";
    private final static String VERSION = "version";
    
    private final static String ACTION = "action";
    private final static String FILTER = "filter";
    
    AsyncTodoRepository client;
    
    TodoAppHandler(AsyncTodoRepository client) {
        this.client = client;
    }
    
    /**
     * Get the to-do list, filtering by the completion status: all, open or completed.
     */
    void getTodos(YokeRequest request) {
        String filter = request.getParameter(FILTER, "all");
        request.put(FILTER, filter);
        
        MessageHandler<JsonArray> onreply = (reply) -> 
    		onDBResult(reply, request, ()->	{
    			request.put("todos", reply.result().body().toArray());
     	        request.response().render("todo-list.hbs");
    		});
               
        switch (filter) {
            case "open":
            case "done":
                Boolean completed = "done".equals(filter);           
                client.findByCompleted(completed, onreply);
                break;
            case "all":
                client.findAll(onreply);
                break;
            default:
                request.response().setStatusCode(500).end(); //should never happen
        }
    }
    
    /**
     * Renders the to-do item edit page.
     */
    void eidtTodo(YokeRequest request) {
        request.exceptionHandler(ex -> onError(request, ex));
        client.findOne(id(request),  
        	(reply) -> onDBResult(reply, request, ()->	{
	 			request.put("todo", reply.result().body().toMap());
	            request.put("isInserting", false);
	            request.response().render("todo-form.hbs");
	 		}));
    }
     
    /**
     * Renders the to-do item creation page.
     */
    void createTodo(YokeRequest request) {
        request.put("isInserting", true);
        request.response().render("todo-form.hbs");
    }
    
    /**
     * Handles the POST action from the to-do form.
     */
    @SuppressWarnings("unchecked")
	void postTodo(YokeRequest request) {
        Long id = id(request);
                   
        @SuppressWarnings("rawtypes")
		MessageHandler onreply = (reply) -> 
        	onDBResult((AsyncResult<?>) reply, request,
        			() -> request.response().redirect("/todos"));
        	
        String action = request.getFormParameter(ACTION, "");
        switch(action) {
            case "save":
                JsonObject entity = getForm(request);               
                client.save(entity, onreply);
                break;
            case "delete":
                client.delete(id, onreply);
                break;
            default:
                request.response().setStatusCode(500).end();
        }      
    }
    
    /** Extract the form data in the request as a JsonObject */
    private JsonObject getForm(YokeRequest request) {
        JsonObject entity = new JsonObject();
        entity.putString(NAME, request.getFormParameter(NAME));
        entity.putString(DESCRIPTION, request.getFormParameter(DESCRIPTION));
        entity.putString(PRIORITY, request.getFormParameter(PRIORITY));
        entity.putBoolean(COMPLETED, "on".equals(request.getFormParameter(COMPLETED)));
        
        Long id = id(request);
        if (id != null) {
            entity.putNumber(ID, id);
        }
        
        String version = request.getFormParameter(VERSION);
        if (version != null && version.isEmpty() == false) {
            entity.putNumber(VERSION, Integer.valueOf(version));
        }

        return entity;
    }
    
    /** Retrieve the to-do item ID from the request */
    private Long id(YokeRequest request) {
    	String strId = request.getParameter(ID, "").trim();
        
    	//The following might throw NumberFormatException, which is ignored
        // for abbrevity in the sample. It should be handled in production code.
        return strId.isEmpty()? null : Long.valueOf(strId);
    }
           
    private void onDBResult(AsyncResult<?> result, 
			YokeRequest request, Runnable work) {
		if (result.succeeded()) {
			work.run();
		} else {
			Throwable ex = result.cause();
			onError(request, ex);
		}
	}
    
    /** Error handler */
    private void onError(YokeRequest request, Throwable ex) {
        String message = null;
        if (ex instanceof ReplyException) {
            ReplyException rex = (ReplyException) ex;
            if (rex.failureCode() == CONFLICT.code()) {
               message = "The record was modified or deleted by someone else.";
            } else if (rex.failureCode() == NOT_FOUND.code()) {
               message = "The record cannot be found.";
            } else if (rex.failureCode() == NOT_ACCEPTABLE.code()) {
               message = rex.getMessage();
            } 
        } 
        message = (message == null) ? "Something went wrong." : message;
        request.put("message", message);
        request.put("exception", ex);
        request.put("url", request.uri());     
        request.response().render("error.hbs");
    }
}
