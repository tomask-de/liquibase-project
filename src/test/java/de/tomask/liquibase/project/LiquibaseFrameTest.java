package de.tomask.liquibase.project;

import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Thomas Kuchs (thomas.kuchs@xcom.de)
 */
public class LiquibaseFrameTest {

    private LiquibaseFrame sut;
    private Connection connection;

    @Before
    public void setUp() throws DatabaseException, ClassNotFoundException, SQLException, NamingException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:", "sa", "secret");
        sut = new LiquibaseFrame(connection);
    }

    @Test
    public void initializeDatabase() throws LiquibaseException, SQLException {
        sut.updateDatabase("single-changelog.xml");
        assertProject();
    }

    @Test
    public void initializeDatabaseComplete() throws LiquibaseException, SQLException {
        sut.updateDatabase();
        assertProject();
        assertAdmin();
    }

    private void assertProject() throws SQLException {
        PreparedStatement preparedStatement;ResultSet resultSet;
        preparedStatement = connection.prepareStatement("SELECT name FROM projects ORDER BY id_project DESC");
        resultSet = preparedStatement.executeQuery();
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("Sample Project", resultSet.getString(1));
        Assert.assertFalse(resultSet.next());
    }

    private void assertAdmin() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM admins ORDER BY id_admin DESC");
        ResultSet resultSet = preparedStatement.executeQuery();
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("admin", resultSet.getString(1));
    }

}
