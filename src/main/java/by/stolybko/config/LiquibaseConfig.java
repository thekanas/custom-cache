package by.stolybko.config;

import by.stolybko.connection.ConnectionPool;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public class LiquibaseConfig {
    public static void migrate() throws LiquibaseException {
        Connection connection = ConnectionPool.get();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new Liquibase("db.changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }
}
