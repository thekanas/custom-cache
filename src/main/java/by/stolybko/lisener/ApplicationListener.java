package by.stolybko.lisener;

import by.stolybko.servlet.OutServlet;
import by.stolybko.servlet.UserServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("by.stolybko");
        context.refresh();

        UserServlet userServlet = context.getBean(UserServlet.class);
        OutServlet outServlet = context.getBean(OutServlet.class);
        ServletContext servletContext = sce.getServletContext();

        servletContext.addServlet("userServlet", userServlet)
                .addMapping("/users");
        servletContext.addServlet("outServlet", outServlet)
                .addMapping("/download");
    }
}
