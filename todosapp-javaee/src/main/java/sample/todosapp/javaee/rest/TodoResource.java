package sample.todosapp.javaee.rest;

import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import sample.todosapp.error.ErrorMessages;
import sample.todosapp.javaee.domain.Todo;
import sample.todosapp.javaee.domain.TodoService;
import sample.todosapp.error.ErrorMessages.Message;

/**
 * REST to-do resource. CRUD operations are executed asynchronously in Jersey.
 * 
 * @author relai
 */

@Path("todos")
@Produces("application/json")
public class TodoResource {

    /** The application service for CRUD operations on Todo entities */
    @Inject private TodoService todoService;
    
    /** The managed executor service for concurrency processing */
    @Resource private ManagedExecutorService executorService;

    @GET
    public void findAll(@Suspended AsyncResponse asyncResponse) {
        //wrap a collection into GenericEntity because of generic erasure at runtime
        run(asyncResponse, () -> new GenericEntity<List<Todo>>(todoService.findAll()){});
    }

    @POST
    public void create(Todo item, @Suspended AsyncResponse asyncResponse) {
        run(asyncResponse, () -> todoService.create(item));
    }

    @GET
    @Path("{id}")
    public void findOne(@PathParam("id") Long id, @Suspended AsyncResponse asyncResponse) {
        run(asyncResponse, () -> todoService.findOne(id));
    }

    @PUT
    @Path("{id}")
    public void update(Todo item, @Suspended AsyncResponse asyncResponse) {
        run(asyncResponse, () -> todoService.update(item));
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id, @Suspended AsyncResponse asyncResponse) {
        run(asyncResponse, () -> {
            todoService.delete(id);
            return Response.noContent().build();
        });
    }
    
    /**
     * Helper method to run asynchronous work.
     * 
     * @param asyncResponse
     * @param work the work to run. The result is returned as the payload.
     */
    private void run(AsyncResponse asyncResponse, Callable work) {
        executorService.submit(() -> {
            try {
                Object response = work.call();
                asyncResponse.resume(response);
            } catch (Exception ex) {
                handleAsynchronousError(ex, asyncResponse);
            }
        });
    }
    
    /**
     * The exception handler. The normal Jersery error handling process is not 
     * available for the asynchronous processing on the executor service threads. 
     * Exceptions are instead handled here.
     * 
     * @param ex
     * @param asyncResponse 
     */
    private void handleAsynchronousError(Exception ex, AsyncResponse asyncResponse) {
        Status status;
        Message msg;
        
        if (ex instanceof EJBException && ex.getCause() instanceof EntityNotFoundException) {
            status = Status.NOT_FOUND;
            msg = ErrorMessages.NOT_FOUND_ERR;
        } else if (ex instanceof EJBException && ex.getCause() instanceof OptimisticLockException) {
            status = Status.CONFLICT;
            msg = ErrorMessages.CONFLICT_ERR;
        } else {
            status = Status.INTERNAL_SERVER_ERROR;
            msg = new Message(ex.getClass().getSimpleName(), ex.getMessage());
        }

        Response resp = Response.status(status).entity(msg).build();
        asyncResponse.resume(resp);
    }
}
