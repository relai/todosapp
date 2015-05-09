package sample.todosapp.spring.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * To route the home page "/" and "/spa" to webjars resources.
 *
 * @author relai
 */
@Controller
@RequestMapping("/")
public class WelcomeController {
    
	
	@RequestMapping(method=GET, value={"/index.html", "/"})
	public void welcome(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException {
        forward( "/webjars/todosapp/index.html", request, response);
	}

    @RequestMapping(method=GET, value="/spa")
	public void  singlePageApplication(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException  {
		forward("/webjars/todosapp/backbone-spa/spa.html", request, response);
	}
    
    private void forward(String file, HttpServletRequest request, 
        HttpServletResponse response) 
        throws ServletException, IOException {
         request.getServletContext()
            .getRequestDispatcher(file)
    		.forward(request,response);
    }  
}
