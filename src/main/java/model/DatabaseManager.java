package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

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

    void create(String tableName, ArrayList<String> namesAndTypesOfColumns);

    void drop(String command);
}
