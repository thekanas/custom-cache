package by.stolybko.config;

import by.stolybko.connection.ConnectionPool;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import java.sql.Connection;

public class LiquibaseConfig {

    public static void migrate() throws LiquibaseException {
        Connection connection = ConnectionPool.get();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
        updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);
        updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, "db.changelog/db.changelog-master.yaml");
        updateCommand.execute();
    }
}
