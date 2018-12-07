package model;

import controller.command.exception.DatabaseManagerException;

import java.sql.*;
import java.util.*;

public class PostgresDatabaseManager implements DatabaseManager {
    private Connection connection;
    private String incorrectInputDataErrorMessage = "Input data does not correct.";

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        closeIfWasConnected();
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            if (e.getMessage().equals("FATAL: password authentication failed for user \"" + userName + "\"")) {
                throw new DatabaseManagerException("Username or password are incorrect.", e);
            } else if (e.getMessage().equals("FATAL: database \"" + databaseName + "\" does not exist")) {
                throw new DatabaseManagerException("Database " + databaseName + " does not exist.", e);
            } else {
                throw new DatabaseManagerException("Houston, we have a problem!...", e);
            }
        }
    }

    @Override
    public Set<String> tables() {
        throwExceptionIfNotConnected();
        String sql = "SELECT table_name FROM information_schema.tables\n" +
                "WHERE table_schema NOT IN ('information_schema', 'pg_catalog')\n" +
                "AND table_schema IN('public');";
        Set<String> tableNames = new HashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }
            return tableNames;
        } catch (SQLException e) {
            throw new DatabaseManagerException();
        }
    }

    @Override
    public void clear(String tableName) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }

    @Override
    public void exit() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException("Connection was not close.", e);
            }
        }
    }

    @Override
    public void delete(String tableName, String verifiableColumnName, String verifiableColumnValue) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName + " WHERE " + verifiableColumnName + " = "
                    + verifiableColumnValue + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }

    @Override
    public void update(String tableName, String verifiableColumnName,
                       String verifiableColumnValue, Map<String, String> namesToValuesOfUpdatableRow) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForUpdatingData(tableName, verifiableColumnName,
                    verifiableColumnValue, namesToValuesOfUpdatableRow);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }


    @Override
    public void insert(String tableName, List<String> columnsNames,
                       List<String> columnsValues) {
        throwExceptionIfSizesOfListsNotEqual(columnsNames, columnsValues);
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForInsertDataInTable(tableName, columnsNames, columnsValues);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }

    @Override
    public List<String> getTableData(String tableName) {
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
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }

    @Override
    public Set<String> getColumnsNamesInTable(String tableName) {
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
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }

    @Override
    public void create(String tableName, List<String> columnsNames,
                       List<String> columnsTypes) {
        throwExceptionIfSizesOfListsNotEqual(columnsNames, columnsTypes);
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForCreatingNewTable(tableName, columnsNames, columnsTypes);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }

    @Override
    public void drop(String tableName) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE " + tableName + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
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
                                         String verifiableColumnValue, Map<String, String> namesToValuesOfUpdatableRow) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        Iterator<Map.Entry<String, String>> iterator = namesToValuesOfUpdatableRow.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            sql.append(next.getKey()).append(" = ").append(next.getValue());
            if (iterator.hasNext()) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(verifiableColumnName).append(" = ").append(verifiableColumnValue).append(";");
        return sql.toString();
    }

    private void throwExceptionIfSizesOfListsNotEqual(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            throw new IllegalArgumentException("You gave wrong arguments.");
        }
    }

    private void throwExceptionIfNotConnected() {
        if (!isConnected()) {
            throw new DatabaseManagerException("You are not connected to database.");
        }
    }

    private void closeIfWasConnected() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException("Previous connection was not close. Try again or reset SQLCmd.", e);
            }
        }
    }
}
