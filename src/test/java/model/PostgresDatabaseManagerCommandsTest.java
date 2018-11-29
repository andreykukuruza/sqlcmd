package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresDatabaseManagerCommandsTest {
    private PostgresDatabaseManager manager;
    private String errorMessage = "Input data does not correct.";

    @BeforeEach
    public void setUp() throws SQLException {
        manager = new PostgresDatabaseManager();
        manager.connect(Config.getCorrectDatabaseName(), Config.getCorrectUserName(), Config.getCorrectPassword());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        manager.exit();
    }

    @Test
    public void tablesTest_WithTablesInDatabase() throws SQLException {
//        given
        manager.create("table1", new ArrayList<>(), new ArrayList<>());
        manager.create("table2", new ArrayList<>(), new ArrayList<>());
        List<String> expected = new ArrayList<>(Arrays.asList("table1", "table2"));
//        when
        ArrayList<String> actual = manager.tables();
//        then
        assertEquals(expected, actual);
//        after
        manager.drop("table1");
        manager.drop("table2");
    }

    @Test
    public void tablesTest_WithoutTablesInDatabase() throws SQLException {
        assertEquals(new ArrayList<>(), manager.tables());
    }

    @Test
    public void clearTest_WithCorrectTableName() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.clear(tableName);
//        then
        assertEquals(new ArrayList<String>(), manager.getTableData(tableName));
//        after
        manager.drop(tableName);
    }

    @Test
    public void clearTest__WithIncorrectTableName() {
        SQLException e = assertThrows(SQLException.class, () -> manager.clear("WrongTableName"));
        assertEquals(errorMessage, e.getMessage());
    }

    @Test
    public void deleteTest_WithCorrectParameters() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.delete(tableName, "age", "19");
//        then
        List<String> actual = manager.getTableData(tableName);
        assertEquals(new ArrayList<>(Arrays.asList("Monica", "13", "1", "Alisa", "21", "4")), actual);
//        after
        manager.drop(tableName);
    }

    @Test
    public void deleteTest_WithIncorrectTableName() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.delete(
                "wrongTableName",
                "age",
                "15"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void deleteTest_WithIncorrectColumnName() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.delete(
                "test",
                "WrongColumnName",
                "19"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void deleteTest_WithIncorrectColumnType() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.delete(
                "test",
                "name",
                "4242424242"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void updateTest_WithCorrectParameters() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.update(tableName,
                "age",
                "19",
                Arrays.asList("name", "id", "age"),
                Arrays.asList("'DELETED'", "-1", "-1"));
//        then
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList(
                "Monica", "13", "1",
                "Alisa", "21", "4",
                "DELETED", "-1", "-1",
                "DELETED", "-1", "-1");
        assertEquals(actual, expected);
//        after
        manager.drop(tableName);
    }

    @Test
    public void updateTest_WithIncorrectTableName() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.update(
                "IncorrectTableName",
                "age",
                "19",
                Arrays.asList("name", "id", "age"),
                Arrays.asList("'DELETED'", "-1", "-1")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void updateTest_WithIncorrectVerifiableColumnName() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.update(
                tableName,
                "IncorrectVerifiableColumnName",
                "19",
                Arrays.asList("name", "id", "age"),
                Arrays.asList("'DELETED'", "-1", "-1")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void updateTest_WithIncorrectVerifiableColumnValue() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.update(
                tableName,
                "age",
                "-5",
                Arrays.asList("name", "id", "age"),
                Arrays.asList("'DELETED'", "-1", "-1"));
//        then
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList(
                "Monica", "13", "1",
                "Alex", "19", "2",
                "Alisa", "19", "3",
                "Alisa", "21", "4");
        assertEquals(actual, expected);
//        after
        manager.drop(tableName);
    }

    @Test
    public void updateTest_WithIncorrectUpdatableColumnsNames() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.update(
                tableName,
                "age",
                "19",
                Arrays.asList("IncorrectUpdatableColumnName", "id", "age"),
                Arrays.asList("'DELETED'", "-1", "-1")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void updateTest_WithIncorrectUpdatableColumnsTypes() throws SQLException {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.update(
                tableName,
                "age",
                "19",
                Arrays.asList("IncorrectUpdatableColumnName", "id", "age"),
                Arrays.asList("'DELETED'", "'IncorrectColumnType'", "-1")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void insertTest_WithCorrectParameters() throws SQLException {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        manager.insert(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("'Lisa'", "22", "42"));
//        then
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList("Lisa", "22", "42");
        assertEquals(actual, expected);
//        after
        manager.drop(tableName);
    }

    @Test
    public void insertTest_WithIncorrectTableName() throws SQLException {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.insert(
                "IncorrectTableName",
                Arrays.asList("name", "age", "id"),
                Arrays.asList("'Lisa'", "22", "42")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void insertTest_WithIncorrectColumnName() throws SQLException {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.insert(
                tableName,
                Arrays.asList("IncorrectColumnName", "age", "id"),
                Arrays.asList("'Lisa'", "22", "42")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void insertTest_WithIncorrectColumnType() throws SQLException {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.insert(
                tableName,
                Arrays.asList("name", "age", "id"),
                Arrays.asList("'Lisa'", "'IncorrectColumnType'", "42")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    private void createTableWithData(String tableName) throws SQLException {
        ArrayList<String> columnsNames = new ArrayList<>(Arrays.asList("name", "age", "id"));
        ArrayList<String> columnsTypes = new ArrayList<>(Arrays.asList("text", "int", "int"));
        manager.create(tableName, columnsNames, columnsTypes);
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Monica'", "13", "1")));
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Alex'", "19", "2")));
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Alisa'", "19", "3")));
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Alisa'", "21", "4")));
    }
}