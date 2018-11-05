package model;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public interface DatabaseManager {
    boolean isConnected();

    void connect(String databaseName, String username, String password) throws SQLException;

    HashSet<String> tables() throws SQLException;

    void clear(String tableName) throws SQLException;

    void exit() throws SQLException;

    void delete(String command);

    void update(String command);

    void insert(String command);

    void find(String command);

    void create(String tableName, List<String> namesOfColumns, List<String> typesOfColumns) throws SQLException;

    void drop(String tableName) throws SQLException;
}
