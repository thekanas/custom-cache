package by.stolybko.listener;

import by.stolybko.config.LiquibaseConfig;
import by.stolybko.util.PropertiesManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import liquibase.exception.LiquibaseException;

@WebListener
public class LiquibaseListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if("true".equals(PropertiesManager.get("databaseAutoInitialization"))) {
            try {
                LiquibaseConfig.migrate();
            } catch (LiquibaseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
