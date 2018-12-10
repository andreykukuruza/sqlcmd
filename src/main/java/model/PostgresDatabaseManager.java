package model;

import model.exception.DatabaseManagerException;

import java.sql.*;
import java.util.*;

import static model.sqlQueries.PostgresSqlQueries.*;

public class PostgresDatabaseManager implements DatabaseManager {
    private Connection connection;
    private static final String WRONG_INPUT_DATA_ERR_MSG = "Input data does not correct.";
    private static final String WRONG_NAME_OR_PASSWORD_ERR_MSG = "Username or password are incorrect.";
    private static final String WRONG_DB_NAME_ERR_MSG = "Database %s does not exist.";
    private static final String CLOSE_CONNECTION_ERR_MSG = "Connection was not close.";
    private static final String NEED_CONNECTION_ERR_MSG = "You are not connected to database.";
    private static final String UNKNOWN_ERR_MSG = "Houston, we have a problem!...";

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        closeIfWasConnected();
        String url = String.format(URL, databaseName);
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            if (e.getMessage().equals(String.format(NAME_OR_PASS_WRONG_ERR_MSG, userName))) {
                throw new DatabaseManagerException(WRONG_NAME_OR_PASSWORD_ERR_MSG, e);
            } else if (e.getMessage().equals(String.format(WRONG_DATABASE_NAME_ERR_MSG, databaseName))) {
                throw new DatabaseManagerException(String.format(WRONG_DB_NAME_ERR_MSG, databaseName), e);
            }
            throw new DatabaseManagerException(UNKNOWN_ERR_MSG, e);
        }
    }

    @Override
    public Set<String> tables() {
        throwExceptionIfNotConnected();
        Set<String> tableNames = new HashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_TABLES_NAMES)) {
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
            String sql = String.format(CLEAR_TABLE, tableName);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }

    @Override
    public void exit() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException(CLOSE_CONNECTION_ERR_MSG, e);
            }
        }
    }

    @Override
    public void delete(String tableName, String verifiableColumnName, String verifiableColumnValue) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(DELETE_FROM_TABLE_ROWS,
                    tableName, verifiableColumnName, verifiableColumnValue);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
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
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }


    @Override
    public void insert(String tableName, Map<String, String> columnNameToColumnValue) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForInsertDataInTable(tableName, columnNameToColumnValue);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }

    @Override
    public List<String> getTableData(String tableName) {
        throwExceptionIfNotConnected();
        String sql = String.format(SELECT_ALL_DATA_IN_TABLE, tableName);
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
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }

    @Override
    public Set<String> getColumnsNamesInTable(String tableName) {
        throwExceptionIfNotConnected();
        String sql = String.format(SELECT_ALL_DATA_IN_TABLE, tableName);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            Set<String> result = new LinkedHashSet<>();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                result.add(resultSetMetaData.getColumnName(i + 1));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }

    @Override
    public void create(String tableName, Map<String, String> columnNameToColumnType) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = getSQLForCreatingNewTable(tableName, columnNameToColumnType);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }

    @Override
    public void drop(String tableName) {
        throwExceptionIfNotConnected();
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(DROP_TABLE, tableName);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(WRONG_INPUT_DATA_ERR_MSG, e);
        }
    }

    private void throwExceptionIfNotConnected() {
        if (!isConnected()) {
            throw new DatabaseManagerException(NEED_CONNECTION_ERR_MSG);
        }
    }

    private void closeIfWasConnected() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException(CLOSE_CONNECTION_ERR_MSG, e);
            }
        }
    }
}
