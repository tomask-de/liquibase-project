package de.tomask.liquibase.project;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.InputStream;
import java.sql.Connection;

/**
 * @author Thomas Kuchs (thomas.kuchs@xcom.de)
 */
public class LiquibaseFrame {

    private final Database database;
    private final de.tomask.liquibase.modul.LiquibaseFrame modul;

    public LiquibaseFrame(final Connection connection) throws DatabaseException {
        this.database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        modul = new de.tomask.liquibase.modul.LiquibaseFrame(connection);
    }

    public void updateDatabase(final String changelog) throws LiquibaseException {
        Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }

    public void updateDatabase() throws LiquibaseException {
        modul.updateDatabase();
        Liquibase project = new Liquibase("single-changelog.xml", new ClassLoaderResourceAccessor(), database);
        project.update("");

    }
}
