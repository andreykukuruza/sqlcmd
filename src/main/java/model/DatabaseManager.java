package model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {
    boolean isConnected();

    void connect(String databaseName, String username, String password);

    Set<String> tables();

    void clear(String tableName);

    void exit();

    void delete(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn);

    void update(String tableName, String nameOfVerifiableColumn, String valueOfVerifiableColumn,
                Map<String, String> columnNameToColumnValueOfUpdatableRows);

    void insert(String tableName, Map<String, String> columnNameToColumnValue);

    List<String> getTableData(String tableName);

    Set<String> getColumnsNamesInTable(String tableName);

    void create(String tableName, Map<String, String> columnNameToColumnType);

    void drop(String tableName);
}
