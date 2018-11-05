package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;
    private String youAreNotConnectedToDatabaseErrorMessage = "You are not connected to database.";

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

    @Override
    public ArrayList<String> tables() throws SQLException {
        if (isConnected()) {
            String sql = "SELECT table_name FROM information_schema.tables\n" +
                    "WHERE table_schema NOT IN ('information_schema', 'pg_catalog')\n" +
                    "AND table_schema IN('public');";
            ArrayList<String> tableNames = new ArrayList<>();

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString(1));
                }
                return tableNames;
            }
        } else {
            throw new SQLException(youAreNotConnectedToDatabaseErrorMessage);
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
            throw new SQLException(youAreNotConnectedToDatabaseErrorMessage);
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
    public void create(String tableName, List<String> namesOfColumns, List<String> typesOfColumns) throws SQLException {
        if (namesOfColumns.size() != typesOfColumns.size()) {
            throw new IllegalArgumentException("Can not create new table with difference number of column names and column types.");
        }
        if (isConnected()) {
            try (Statement statement = connection.createStatement()) {
                String sql = getSQLForCreatingNewTable(tableName, namesOfColumns, typesOfColumns);
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                if (e.getMessage().equals("ERROR: relation \"test\" already exists")) {
                    throw new SQLException("Table with name " + tableName + " already exists. You can not create the table with the same name.", e);
                } else if (e.getMessage().startsWith("ERROR: type")) {
                    throw new SQLException("You used wrong type for one or more columns.", e);
                } else if(e.getMessage().startsWith("ERROR: syntax error at or near")) {
                    throw new SQLException("You used wrong name for one or more columns.", e);
                }else {
                    throw e;
                }
            }
        } else {
            throw new SQLException(youAreNotConnectedToDatabaseErrorMessage);
        }
    }

    private String getSQLForCreatingNewTable(String tableName, List<String> namesOfColumns, List<String> typesOfColumns) {
        String sql = "CREATE TABLE public." + tableName + " (";

        if (namesOfColumns.size() != 0) {
            for (int i = 0; i < namesOfColumns.size() - 1; i++) {
                sql += namesOfColumns.get(i) + " " + typesOfColumns.get(i) + ", ";
            }
            sql += namesOfColumns.get(namesOfColumns.size() - 1) + " " + typesOfColumns.get(typesOfColumns.size() - 1);
        }
        sql += ");";
        return sql;
    }

    @Override
    public void drop(String tableName) throws SQLException {
        if (isConnected()) {
            try (Statement statement = connection.createStatement()) {
                String sql = "DROP TABLE " + tableName + ";";
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                if (e.getMessage().equals("ERROR: table \"" + tableName + "\" does not exist")) {
                    throw new SQLException("Table " + tableName + " does not exist.");
                }
                System.out.println(e.getMessage());
            }
        } else {
            throw new SQLException(youAreNotConnectedToDatabaseErrorMessage);
        }
    }
}
