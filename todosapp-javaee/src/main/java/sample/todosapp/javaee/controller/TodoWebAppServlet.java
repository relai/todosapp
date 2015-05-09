package sample.todosapp.javaee.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sample.todosapp.error.ErrorMessages;
import sample.todosapp.javaee.domain.Todo;
import sample.todosapp.javaee.domain.TodoService;

/**
 * To-do web application, rendering JSP pages asynchronously in Servlet.
 * 
 * @author relai
 */
@WebServlet(urlPatterns = {"/todos/*"}, asyncSupported = true)
public class TodoWebAppServlet extends HttpServlet {
    
    /** The application service for CRUD operations on Todo entities */
    @Inject private TodoService todoService;
    
    /** The managed executor service for concurrency processing */
    @Resource private ManagedExecutorService executorService;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        switch (pathInfo != null? pathInfo : "") {
            case "":
                getTodoList(request, response);
                break;
            case "/create":
                getCreateTodoForm(request, response);
                break;
            default:
                 getEditTodoForm(request, response);
        }
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action");
        switch (action != null? action : "") {
            case "delete":
                deleteTodo(request, response);
                break;
            case "save":
                saveTodo(request, response);
                break;
            default:
                throw new ServletException("Unknown action");
        }
    }

    /**
     *  Get the to-do list, filtered by the completion status (all, open and done).
     */
    private void getTodoList(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext ctx = request.startAsync();
        runAsync(ctx, () -> {
            List<Todo> todos;
            String filter = request.getParameter("filter");
            switch (filter != null? filter : "") {
                case "open":
                case "done":
                    todos = todoService.findByCompleted("done".equals(filter));
                    break;
                case "all":
                default:
                    todos = todoService.findAll();
                    filter = "all";
            }
            request.setAttribute("todos", todos);
            request.setAttribute("filter", filter);
            ctx.dispatch("/WEB-INF/jsp/todo-list.jsp");
        });
    }
    
    /**
     *  Renders the to-do item creation form.
     */   
    private void getCreateTodoForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext()
            .getRequestDispatcher("/WEB-INF/jsp/todo-form.jsp");
        dispatcher.forward(request, response);
    }

    /**
     *  Renders the to-do item edit form.
     */ 
    private void getEditTodoForm(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext ctx = request.startAsync();
        runAsync(ctx, () -> {
            Long id = id(request);
            Todo item = todoService.findOne(id);
            request.setAttribute("todo", item);
            ctx.dispatch("/WEB-INF/jsp/todo-form.jsp");
        });
    }

    /**
     *  Save a to-do item. It can either create or update a to-do item.
     */ 
    private void saveTodo(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext ctx = request.startAsync();
        runAsync(ctx, () -> {
            Todo item = new Todo();
            item.setDescription(request.getParameter("description"));
            item.setName(request.getParameter("name"));
            item.setPriority(request.getParameter("priority"));
            item.setCompleted("on".equals(request.getParameter("completed")));  
            
            if ("/create".equals(request.getPathInfo())) {
                todoService.create(item);
            } else {
                item.setId(id(request));
                item.setVersion(Integer.valueOf(request.getParameter("version")));
                todoService.update(item);
            }

            redirect(ctx, "/todos");
            ctx.complete();
        });
    }

    /**
     *  Delete a to-do item.
     */ 
    private void deleteTodo(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext ctx = request.startAsync();
        runAsync(ctx, () -> {
            Long id = id(request);
            todoService.delete(id);
            redirect(ctx, "/todos");
            ctx.complete();
        });
    }
    
    /**
     *  Extract id from the Http request.
     */ 
    private Long id(HttpServletRequest request) {
        Long id = null;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String idString = pathInfo.substring(1);
            // This can throw NumberFormatException
             id = Long.valueOf(idString);
        }
        return id;
    }
    
    /**
     *  A helper method to wrap the sendRedirect with unchecked IO exceptions.
     */ 
    private void redirect(AsyncContext ctx, String redirectLocation) {
        try {
            ((HttpServletResponse) ctx.getResponse()).sendRedirect(redirectLocation);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     *  A helper method to run asynchronous work.
     */     
    private void runAsync(AsyncContext ctx, Runnable work) {
        executorService.submit(() -> {
            try {
                work.run();
            } catch (RuntimeException ex) {
                handleAsynchronousError(ex, ctx);
            }
        });
    }

    /**
     * Error handler for asynchronous work. Exceptions in the executor service 
     * threads cannot be handled by the standard JSP error handling mechanism.  
     * Instead, we handle all exceptions here.
     */ 
    private void handleAsynchronousError(RuntimeException ex, AsyncContext ctx) {
        String message;
       if (ex instanceof EJBException && ex.getCause() instanceof EntityNotFoundException) {
            message = ErrorMessages.NOT_FOUND_MSG;
        } else if (ex instanceof EJBException && ex.getCause() instanceof OptimisticLockException) {
            message = ErrorMessages.CONFLICT_MSG;
        } else {
            message = "Something went wrong.";
        }
        HttpServletRequest req = (HttpServletRequest) ctx.getRequest();
        req.setAttribute("message", message);
        req.setAttribute("exception", ex);
        req.setAttribute("url", req.getRequestURI());
        ctx.dispatch("/WEB-INF/jsp/error.jsp");
    }
}
