package model;

import java.sql.*;
import java.util.HashSet;

public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;

    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void connect(String databaseName, String username, String password) throws Exception {
        closeIfWasConnected();

        String url = "jdbc:postgresql://localhost:5432/" + databaseName;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            if (e.getMessage().equals("FATAL: password authentication failed for user \"" + username + "\"")) {
                throw new Exception("Username or password are incorrect.", e);
            } else if (e.getMessage().equals("FATAL: database \"" + databaseName + "\" does not exist")) {
                throw new Exception("Database " + databaseName + " does not exist.", e);
            } else {
                throw e;
            }
        }
    }

    private void closeIfWasConnected() throws Exception {
        if (isConnected()) {
            try {
                connection.close();
            } catch (Exception e) {
                throw new Exception("Previous connection was not close. Try again or reset SQLCmd.", e);
            }
        }
    }

    @Override
    public HashSet<String> tables() throws SQLException {
        String sql = "SELECT table_name FROM information_schema.tables\n" +
                "WHERE table_schema NOT IN ('information_schema', 'pg_catalog')\n" +
                "AND table_schema IN('public');";
        HashSet<String> tableNames = new HashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql))
        {
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }
            return tableNames;
        }
    }

    @Override
    public void clear(String tableName) throws Exception {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName + ";";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            if (e.getMessage().equals("ERROR: relation \"" + tableName + "\" does not exist\n" + "  Позиция: 13")) {
                throw new Exception("Table " + tableName + " does not exist.", e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void exit() throws Exception {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new Exception("Connection was not close.", e);
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
    public void create(String command) {

    }

    @Override
    public void drop(String command) {

    }
}


//        String url = "jdbc:postgresql://localhost:5432/sqlcmdDB?currentSchema=sqlcmdDB";
//        String login = "postgres";
//        String password = "sup3r42pass";
//        Connection connection = null;
//
//        String sql = "CREATE TABLE public.COMPANY " +
//                "(ID INT PRIMARY KEY     NOT NULL," +
//                " NAME           TEXT    NOT NULL, " +
//                " AGE            INT     NOT NULL, " +
//                " ADDRESS        CHAR(50), " +
//                " SALARY         REAL);";
//
//        try {
//            connection = DriverManager.getConnection(url, login, password);
//            Statement statement = connection.createStatement();
//            statement.execute(sql);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }