package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;

    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void connect(String databaseName, String userName, String password) throws SQLException {
        closeIfWasConnected();

        String url = "jdbc:postgresql://localhost:5432/" + databaseName;
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            if (e.getMessage().equals("FATAL: password authentication failed for user \"" + userName + "\"")) {
                throw new SQLException("Username or password are incorrect.", e);
            } else if (e.getMessage().equals("FATAL: database \"" + databaseName + "\" does not exist")) {
                throw new SQLException("Database " + databaseName + " does not exist.", e);
            } else {
                throw e;
            }
        }
    }

    private void closeIfWasConnected() throws SQLException {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new SQLException("Previous connection was not close. Try again or reset SQLCmd.", e);
            }
        }
    }

    @Override
    public HashSet<String> tables() throws SQLException {
        if (isConnected()) {
            String sql = "SELECT table_name FROM information_schema.tables\n" +
                    "WHERE table_schema NOT IN ('information_schema', 'pg_catalog')\n" +
                    "AND table_schema IN('public');";
            HashSet<String> tableNames = new HashSet<>();

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString(1));
                }
                return tableNames;
            }
        } else {
            throw new SQLException("You are not connected to database");
        }
    }

    @Override
    public void clear(String tableName) throws SQLException {
        if (isConnected()) {
            try (Statement statement = connection.createStatement()) {
                String sql = "DELETE FROM " + tableName + ";";
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                if (e.getMessage().equals("ERROR: relation \"" + tableName + "\" does not exist\n" + "  Позиция: 13")) {
                    throw new SQLException("Table " + tableName + " does not exist.", e);
                } else {
                    throw e;
                }
            }
        } else {
            throw new SQLException("You are not connected to database");
        }
    }

    @Override
    public void exit() throws SQLException {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new SQLException("Connection was not close.", e);
            }
        }
    }

    @Override
    public void delete(String command) {

    }

    @Override
    public void update(String command) {

    }

    @Override
    public void insert(String command) {

    }

    @Override
    public void find(String command) {

    }

    @Override
    public void create(String tableName, ArrayList<String> namesAndTypesOfColumns) {

    }

    @Override
    public void drop(String command) {

    }
}

