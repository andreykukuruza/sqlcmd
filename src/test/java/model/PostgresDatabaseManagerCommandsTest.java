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
    public void clearTest_WithCorrectNameOfTable() throws SQLException {
//        given
        String tableName = "test";
        ArrayList<String> namesOfColumns = new ArrayList<>(Arrays.asList("name", "age"));
        ArrayList<String> typesOfColumns = new ArrayList<>(Arrays.asList("text", "int"));
        manager.create(tableName, namesOfColumns, typesOfColumns);
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'Monica'", "13")));
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'John'", "15")));
//        when
        manager.clear(tableName);
//        then
        assertEquals(new ArrayList<String>(), manager.getTableData(tableName));
//        after
        manager.drop(tableName);
    }

    @Test
    public void clearTest__WithIncorrectNameOfTable() {
        SQLException e = assertThrows(SQLException.class, () ->
                manager.clear("WrongTableName"));
        assertEquals("Input data does not correct.", e.getMessage());
    }

    @Test
    public void deleteTest_WithCorrectParameters() throws SQLException {
//        given
        String tableName = "test";
        ArrayList<String> namesOfColumns = new ArrayList<>(Arrays.asList("name", "age"));
        ArrayList<String> typesOfColumns = new ArrayList<>(Arrays.asList("text", "int"));
        manager.create(tableName, namesOfColumns, typesOfColumns);
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'Monica'", "13")));
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'John'", "15")));
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'Mike'", "15")));
//        when
        manager.delete(tableName, "age", "15");
//        then
        List<String> actual = manager.getTableData(tableName);
        assertEquals(new ArrayList<>(Arrays.asList("Monica", "13")), actual);
//        after
        manager.drop(tableName);
    }

    @Test
    public void deleteTest_WithIncorrectTableName() {
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.delete(
                "wrongTableName",
                "age",
                "15"));
//        then
        assertEquals("Input data does not correct.", e.getMessage());
    }

    @Test
    public void deleteTest_WithIncorrectColumnName() throws SQLException {
//        given
        String tableName = "test";
        ArrayList<String> namesOfColumns = new ArrayList<>(Arrays.asList("name", "age"));
        ArrayList<String> typesOfColumns = new ArrayList<>(Arrays.asList("text", "int"));
        manager.create(tableName, namesOfColumns, typesOfColumns);
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'Monica'", "13")));
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.delete(
                "test",
                "WrongColumnName",
                "15"));
//        then
        assertEquals("Input data does not correct.", e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    public void deleteTest_WithIncorrectColumnType() throws SQLException {
//        given
        String tableName = "test";
        ArrayList<String> namesOfColumns = new ArrayList<>(Arrays.asList("name", "age"));
        ArrayList<String> typesOfColumns = new ArrayList<>(Arrays.asList("text", "int"));
        manager.create(tableName, namesOfColumns, typesOfColumns);
        manager.insert(tableName, namesOfColumns, new ArrayList<>(Arrays.asList("'Monica'", "13")));
//        when
        SQLException e = assertThrows(SQLException.class, () -> manager.delete(
                "test",
                "name",
                "4242424242"));
//        then
        assertEquals("Input data does not correct.", e.getMessage());
//        after
        manager.drop(tableName);
    }
}
