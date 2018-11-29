package model;

import java.sql.*;
import java.util.*;

public class PostgresDatabaseManager implements DatabaseManager {
    private Connection connection;
    private String inputDataDoesNotCorrectErrorMessage = "Input data does not correct.";

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
                throw new SQLException("Houston, we have a problem!...", e);
            }
        }
    }

    @Override
    public ArrayList<String> tables() throws SQLException {
        throwExceptionIfNotConnected();
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
    }

    @Override
    public void clear(String tableName) throws SQLException {
        throwExceptionIfNotConnected();

        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
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
    public void delete(String tableName, String verifiableColumnName,
                       String verifiableColumnValue) throws SQLException {
        throwExceptionIfNotConnected();

        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName + " WHERE " + verifiableColumnName + " = "
                    + verifiableColumnValue + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
        }
    }

    @Override
    public void update(String tableName, String verifiableColumnName,
                       String verifiableColumnValue, List<String> updatableColumnsNames,
                       List<String> updatableColumnsValues) throws SQLException {
        throwExceptionIfSizesOfListsNotEqual(updatableColumnsNames, updatableColumnsValues);
        throwExceptionIfNotConnected();

        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForUpdatingData(tableName, verifiableColumnName,
                    verifiableColumnValue, updatableColumnsNames, updatableColumnsValues);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
        }
    }


    @Override
    public void insert(String tableName, List<String> columnNames,
                       List<String> columnValues) throws SQLException {
        throwExceptionIfSizesOfListsNotEqual(columnNames, columnValues);
        throwExceptionIfNotConnected();

        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForInsertDataInTable(tableName, columnNames, columnValues);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
        }
    }

    @Override
    public List<String> getTableData(String tableName) throws SQLException {
        throwExceptionIfNotConnected();

        String sql = "SELECT * FROM public." + tableName + ";";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            List<String> result = new ArrayList<>();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    result.add(resultSet.getObject(i + 1).toString());
                }
            }
            return result;
        } catch (SQLException e) {
            if (e.getMessage().startsWith("ERROR: relation \"public."
                    + tableName + "\" does not exist")) {
                throw new SQLException("Table " + tableName + " does not exist.");
            } else if (e.getMessage().startsWith("ERROR: syntax error at or near")) {
                throw new SQLException("Syntax error in table name.");
            }
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
        }
    }

    @Override
    public Set<String> getColumnNamesInTable(String tableName) throws SQLException {
        throwExceptionIfNotConnected();

        String sql = "SELECT * FROM public." + tableName + ";";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            Set<String> result = new LinkedHashSet<>();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                result.add(resultSetMetaData.getColumnName(i + 1));
            }

            return result;
        } catch (SQLException e) {
            if (e.getMessage().startsWith("ERROR: relation \"public." + tableName + "\" does not exist")) {
                throw new SQLException("Table " + tableName + " does not exist.");
            } else if (e.getMessage().startsWith("ERROR: syntax error at or near")) {
                throw new SQLException("Syntax error in table name.");
            }
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
        }
    }

    @Override
    public void create(String tableName, List<String> columnsNames,
                       List<String> columnsTypes) throws SQLException {
        throwExceptionIfSizesOfListsNotEqual(columnsNames, columnsTypes);
        throwExceptionIfNotConnected();

        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForCreatingNewTable(tableName, columnsNames, columnsTypes);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException(inputDataDoesNotCorrectErrorMessage, e);
        }
    }

    @Override
    public void drop(String tableName) throws SQLException {
        throwExceptionIfNotConnected();

        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE " + tableName + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            if (e.getMessage().equals("ERROR: table \"" + tableName + "\" does not exist")) {
                throw new SQLException("Table " + tableName + " does not exist.");
            }
            System.out.println(e.getMessage());
        }
    }

    private String getSQLForCreatingNewTable(String tableName, List<String> columnsNames,
                                             List<String> columnsTypes) {
        StringBuilder sql = new StringBuilder("CREATE TABLE public." + tableName + " (");

        if (columnsNames.size() != 0) {
            for (int i = 0; i < columnsNames.size() - 1; i++) {
                sql.append(columnsNames.get(i)).append(" ").append(columnsTypes.get(i)).append(", ");
            }
            sql.append(columnsNames.get(columnsNames.size() - 1)).append(" ");
            sql.append(columnsTypes.get(columnsTypes.size() - 1));
        }
        sql.append(");");
        return sql.toString();
    }

    private String getSQLForInsertDataInTable(String tableName, List<String> columnNames, List<String> columnValues) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < columnNames.size() - 1; i++) {
            sql.append(columnNames.get(i)).append(", ");
        }
        sql.append(columnNames.get(columnNames.size() - 1)).append(") VALUES (");
        for (int i = 0; i < columnValues.size() - 1; i++) {
            sql.append(columnValues.get(i)).append(", ");
        }
        sql.append(columnValues.get(columnValues.size() - 1)).append(");");
        return sql.toString();
    }

    private String getSQLForUpdatingData(String tableName, String verifiableColumnName,
                                         String verifiableColumnValue, List<String> updatableColumnsNames,
                                         List<String> updatableColumnsValues) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < updatableColumnsNames.size() - 1; i++) {
            sql.append(updatableColumnsNames.get(i)).append(" = ").append(updatableColumnsValues.get(i)).append(", ");
        }
        sql.append(updatableColumnsNames.get(updatableColumnsNames.size() - 1));
        sql.append(" = ").append(updatableColumnsValues.get(updatableColumnsValues.size() - 1));
        sql.append(" WHERE ").append(verifiableColumnName).append(" = ").append(verifiableColumnValue).append(";");
        return sql.toString();
    }

    private void throwExceptionIfSizesOfListsNotEqual(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            throw new IllegalArgumentException("You gave wrong arguments.");
        }
    }

    private void throwExceptionIfNotConnected() throws SQLException {
        if (!isConnected()) {
            throw new SQLException("You are not connected to database.");
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
}
