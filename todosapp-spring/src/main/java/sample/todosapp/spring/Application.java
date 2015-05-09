package sample.todosapp.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableSpringDataWebSupport

/**
 * Spring boot application class. 
 * 
 * @author relai
 */
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
