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
                       String verifiableColumnValue, Map<String, String> columnNameToColumnValueOfUpdatableRows) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForUpdatingData(tableName, verifiableColumnName,
                    verifiableColumnValue, columnNameToColumnValueOfUpdatableRows);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(incorrectInputDataErrorMessage, e);
        }
    }


    @Override
    public void insert(String tableName, Map<String, String> columnNameToColumnValue) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForInsertDataInTable(tableName, columnNameToColumnValue);
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
    public void create(String tableName, Map<String, String> columnNameToColumnType) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForCreatingNewTable(tableName, columnNameToColumnType);
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

    private String getSQLForCreatingNewTable(String tableName, Map<String, String> columnNameToColumnType) {
        StringBuilder sql = new StringBuilder("CREATE TABLE public." + tableName + " (");
        if (columnNameToColumnType.size() != 0) {
            Iterator<Map.Entry<String, String>> iterator = columnNameToColumnType.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                sql.append(next.getKey()).append(" ").append(next.getValue());
                if (iterator.hasNext()) {
                    sql.append(", ");
                }
            }
        }
        return sql.append(");").toString();
    }

    private String getSQLForInsertDataInTable(String tableName, Map<String, String> columnNameToColumnValue) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        Iterator<String> keyIterator = columnNameToColumnValue.keySet().iterator();
        while (keyIterator.hasNext()) {
            sql.append(keyIterator.next());
            if (keyIterator.hasNext()) {
                sql.append(", ");
            }
        }
        sql.append(") VALUES (");
        Iterator<String> valueIterator = columnNameToColumnValue.values().iterator();
        while (valueIterator.hasNext()) {
            sql.append(valueIterator.next());
            if (valueIterator.hasNext()) {
                sql.append(", ");
            }
        }
        return sql.append(");").toString();
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
