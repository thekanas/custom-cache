package by.stolybko.config;

import by.stolybko.api.DeSerializer;
import by.stolybko.api.Serializer;
import by.stolybko.api.impl.DeSerializerImpl;
import by.stolybko.api.impl.SerializerImpl;
import by.stolybko.cache.Cache;
import by.stolybko.cache.CacheFactory;
import by.stolybko.entity.BaseEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy
public class AppConfig {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${liquibase.changelog}")
    private String changelog;

    @Value("${liquibase.databaseAutoInitialization}")
    private boolean databaseAutoInitialization;

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private String capacity;


    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changelog);
        liquibase.setDataSource(dataSource());
        liquibase.setShouldRun(databaseAutoInitialization);
        return liquibase;
    }

    @Bean
    public DeSerializer deSerializer() {
        return new DeSerializerImpl();
    }

    @Bean
    public Serializer serializer() {
        return new SerializerImpl();
    }

    @Bean
    public Cache<Long, BaseEntity> cache() {
        return CacheFactory.getCache(algorithm, Integer.parseInt(capacity));
    }
}
