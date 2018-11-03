package model;

import java.util.HashSet;

public interface DatabaseManager {
    boolean isConnected();

    void connect(String databaseName, String username, String password) throws Exception;

    HashSet<String> tables() throws Exception;

    void clear(String tableName) throws Exception;

    void exit() throws Exception;

    void delete(String command);

    void update(String command);

    void insert(String command);

    void find(String command);

    void create(String command);

    void drop(String command);
}
