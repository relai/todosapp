package sample.todosapp.javaee.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * To route the home page "index.html" and "spa" to webjars resources.
 *
 * @author relai
 */
@WebServlet(urlPatterns = {"/spa", "/index.html"})
public class WelcomeServlet extends HttpServlet {

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
        String path = request.getServletPath();
        String file = "/spa".equals(path)
            ? "/webjars/todosapp/backbone-spa/spa.html"
            : "/webjars/todosapp/index.html";
        getServletContext()
            .getRequestDispatcher(file)
            .forward(request, response);
    }

}
