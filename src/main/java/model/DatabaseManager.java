package model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    boolean isConnected();

    void connect(String databaseName, String username, String password) throws SQLException;

    List<String> tables() throws SQLException;

    void clear(String tableName) throws SQLException;

    void exit() throws SQLException;

    void delete(String command);

    void update(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn, List<String> namesOfUpdatableColumns, List<String> valuesOfUpdatableColumns) throws SQLException;

    void insert(String tableName, List<String> columnNames, List<String> columnValues) throws SQLException;

    List<String> getTableData(String tableName) throws SQLException;

    Set<String> getColumnNamesInTable(String tableName) throws SQLException;

    void create(String tableName, List<String> namesOfColumns, List<String> typesOfColumns) throws SQLException;

    void drop(String tableName) throws SQLException;
}
