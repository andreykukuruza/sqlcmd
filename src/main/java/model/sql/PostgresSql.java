package model.sql;

import java.util.Iterator;
import java.util.Map;

public class PostgresSql {
    public static final String URL = "jdbc:postgresql://localhost:5432/%s";

    public static final String GET_ALL_TABLES_NAMES = "SELECT table_name FROM information_schema.tables\n" +
            "WHERE table_schema NOT IN ('information_schema', 'pg_catalog')\n" +
            "AND table_schema IN('public');";
    public static final String CLEAR_TABLE = "DELETE FROM %s;";
    public static final String DELETE_FROM_TABLE_ROWS = "DELETE FROM %s WHERE %s = %s;";
    public static final String SELECT_ALL_DATA_IN_TABLE = "SELECT * FROM public.%s;";
    public static final String DROP_TABLE = "DROP TABLE %s;";

    public static final String NAME_OR_PASS_WRONG_ERR_MSG = "FATAL: password authentication failed for user \"%s\"";
    public static final String WRONG_DATABASE_NAME_ERR_MSG = "FATAL: database \"%s\" does not exist";

    private static final String CREATE = "CREATE TABLE public.%s (";
    private static final String INSERT = "INSERT INTO %s (";
    private static final String UPDATE = "UPDATE %s SET ";

    public static String getSQLForCreatingNewTable(String tableName, Map<String, String> columnNameToColumnType) {
        StringBuilder sql = new StringBuilder(String.format(CREATE, tableName));
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

    public static String getSQLForInsertDataInTable(String tableName, Map<String, String> columnNameToColumnValue) {
        StringBuilder sql = new StringBuilder(String.format(INSERT, tableName));
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

    public static String getSQLForUpdatingData(String tableName, String verifiableColumnName,
                                               String verifiableColumnValue,
                                               Map<String, String> namesToValuesOfUpdatableRow) {
        StringBuilder sql = new StringBuilder(String.format(UPDATE, tableName));
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
}
