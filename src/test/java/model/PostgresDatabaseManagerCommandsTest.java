package model;

import config.Config;
import controller.command.exception.DatabaseManagerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PostgresDatabaseManagerCommandsTest {
    private PostgresDatabaseManager manager;
    private String errorMessage = "Input data does not correct.";

    @BeforeEach
    void setUp() {
        manager = new PostgresDatabaseManager();
        manager.connect(Config.getDatabaseName(), Config.getUserName(), Config.getPassword());
    }

    @AfterEach
    void tearDown() {
        manager.exit();
    }

    @Test
    void tablesTest_WithTablesInDatabase() {
//        given
        manager.create("table1", new ArrayList<>(), new ArrayList<>());
        manager.create("table2", new ArrayList<>(), new ArrayList<>());
//        when
        Set<String> actual = manager.tables();
//        then
        Set<String> expected = new HashSet<String>() {{
            add("table1");
            add("table2");
        }};
        assertEquals(expected, actual);
//        after
        manager.drop("table1");
        manager.drop("table2");
    }

    @Test
    void tablesTest_WithoutTablesInDatabase() {
        assertTrue(manager.tables().isEmpty());
    }

    @Test
    void clearTest_WithCorrectTableName() {
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
    void clearTest__WithIncorrectTableName() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.clear("WrongTableName"));
        assertEquals(errorMessage, e.getMessage());
    }

    @Test
    void deleteTest_WithCorrectParameters() {
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
    void deleteTest_WithIncorrectTableName() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.delete(
                "wrongTableName",
                "age",
                "15"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void deleteTest_WithIncorrectColumnName() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.delete(
                "test",
                "WrongColumnName",
                "19"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void deleteTest_WithIncorrectColumnType() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.delete(
                "test",
                "name",
                "4242424242"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void updateTest_WithCorrectParameters() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.update(tableName,
                "age",
                "19",
                new LinkedHashMap<String, String>() {{
                    put("name", "'DELETED'");
                    put("id", "-1");
                    put("age", "-1");
                }});
//        then
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList(
                "Monica", "13", "1",
                "Alisa", "21", "4",
                "DELETED", "-1", "-1",
                "DELETED", "-1", "-1");
        assertEquals(expected, actual);
//        after
        manager.drop(tableName);
    }

    @Test
    void updateTest_WithIncorrectTableName() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.update(
                "IncorrectTableName",
                "age",
                "19",
                new LinkedHashMap<String, String>() {{
                    put("name", "'DELETED'");
                    put("id", "-1");
                    put("age", "-1");
                }}));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void updateTest_WithIncorrectVerifiableColumnName() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.update(
                tableName,
                "IncorrectVerifiableColumnName",
                "19",
                new LinkedHashMap<String, String>() {{
                    put("name", "'DELETED'");
                    put("id", "-1");
                    put("age", "-1");
                }}));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void updateTest_WithIncorrectVerifiableColumnValue() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.update(
                tableName,
                "age",
                "-5",
                new LinkedHashMap<String, String>() {{
                    put("name", "'DELETED'");
                    put("id", "-1");
                    put("age", "-1");
                }});
//        then
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList(
                "Monica", "13", "1",
                "Alex", "19", "2",
                "Alisa", "19", "3",
                "Alisa", "21", "4");
        assertEquals(expected, actual);
//        after
        manager.drop(tableName);
    }

    @Test
    void updateTest_WithIncorrectUpdatableColumnsNames() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.update(
                tableName,
                "age",
                "19",
                new LinkedHashMap<String, String>() {{
                    put("IncorrectUpdatableColumnsName", "'DELETED'");
                    put("id", "-1");
                    put("age", "-1");
                }}));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void updateTest_WithIncorrectUpdatableColumnsTypes() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.update(
                tableName,
                "age",
                "19",
                new LinkedHashMap<String, String>() {{
                    put("name", "'DELETED'");
                    put("id", "'IncorrectType'");
                    put("age", "-1");
                }}));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void insertTest_WithCorrectParameters() {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        manager.insert(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("'Lisa'", "22", "42"));
//        then
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList("Lisa", "22", "42");
        assertEquals(expected, actual);
//        after
        manager.drop(tableName);
    }

    @Test
    void insertTest_WithIncorrectTableName() {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.insert(
                "IncorrectTableName",
                Arrays.asList("name", "age", "id"),
                Arrays.asList("'Lisa'", "22", "42")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void insertTest_WithIncorrectColumnName() {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.insert(
                tableName,
                Arrays.asList("IncorrectColumnName", "age", "id"),
                Arrays.asList("'Lisa'", "22", "42")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void insertTest_WithIncorrectColumnType() {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class, () -> manager.insert(
                tableName,
                Arrays.asList("name", "age", "id"),
                Arrays.asList("'Lisa'", "'IncorrectColumnType'", "42")));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void getTableDataTest_WithEmptyTable() {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        List<String> actual = manager.getTableData(tableName);
//        then
        assertTrue(actual.isEmpty());
//        after
        manager.drop(tableName);
    }

    @Test
    void getTableDataTest_WithNotEmptyTable() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        List<String> actual = manager.getTableData(tableName);
        List<String> expected = Arrays.asList(
                "Monica", "13", "1",
                "Alex", "19", "2",
                "Alisa", "19", "3",
                "Alisa", "21", "4"
        );
//        then
        assertEquals(expected, actual);
//        after
        manager.drop(tableName);
    }

    @Test
    void getTableDataTest_WithIncorrectTableName() {
//        given
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age", "id"), Arrays.asList("text", "int", "int"));
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class,
                () -> manager.getTableData("IncorrectTableName"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void getColumnsNamesInTableTest_WithoutColumns() {
//        given
        String tableName = "test";
        manager.create(tableName, new ArrayList<>(), new ArrayList<>());
//        when
        Set<String> actual = manager.getColumnsNamesInTable(tableName);
//        then
        assertTrue(actual.isEmpty());
//        after
        manager.drop(tableName);
    }

    @Test
    void getColumnsNamesInTableTest_WithColumns() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        Set<String> actual = manager.getColumnsNamesInTable(tableName);
//        then
        HashSet<String> expected = new HashSet<String>() {{
            add("name");
            add("age");
            add("id");
        }};
        assertEquals(expected, actual);
//        after
        manager.drop(tableName);
    }

    @Test
    void getColumnsNamesInTableTest_WithIncorrectTableName() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class,
                () -> manager.getColumnsNamesInTable("IncorrectTableName"));
//        then
        assertEquals(errorMessage, e.getMessage());
//        after
        manager.drop(tableName);
    }

    @Test
    void createTest_WithCorrectParameters() {
//        when
        String tableName = "test";
        manager.create(tableName, Arrays.asList("name", "age"), Arrays.asList("text", "int"));
//        then
        Set<String> actual = manager.tables();
        Set<String> expected = new HashSet<String>() {{
            add("test");
        }};
        assertEquals(expected, actual);
//        after
        manager.drop(tableName);
    }

    @Test
    void createTest_WithDifferentSizeColumnsAndTypes() {
//        when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> manager.create("test", Arrays.asList("name", "age", "id"), Arrays.asList("text", "int")));
//        then
        assertEquals("You gave wrong arguments.", e.getMessage());
    }

    @Test
    void createTest_WithIncorrectType() {
//        when
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class,
                () -> manager.create("test", Arrays.asList("name", "age"), Arrays.asList("WrongType", "int")));
//        then
        assertEquals(errorMessage, e.getMessage());
    }

    @Test
    void dropTest_WithCorrectTableName() {
//        given
        String tableName = "test";
        createTableWithData(tableName);
//        when
        manager.drop(tableName);
//        then
        assertTrue(manager.tables().isEmpty());
    }

    @Test
    void dropTable_WithIncorrectTableName() {
        DatabaseManagerException e = assertThrows(DatabaseManagerException.class,
                () -> manager.drop("WrongTableName"));
        assertEquals(errorMessage, e.getMessage());
    }

    private void createTableWithData(String tableName) {
        ArrayList<String> columnsNames = new ArrayList<>(Arrays.asList("name", "age", "id"));
        ArrayList<String> columnsTypes = new ArrayList<>(Arrays.asList("text", "int", "int"));
        manager.create(tableName, columnsNames, columnsTypes);
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Monica'", "13", "1")));
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Alex'", "19", "2")));
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Alisa'", "19", "3")));
        manager.insert(tableName, columnsNames, new ArrayList<>(Arrays.asList("'Alisa'", "21", "4")));
    }
}
