package config;

/**
 * To successfully pass the tests, you need to create an empty database.
 * In the fields indicate the name of this database, login and password.
 **/

public class Config {
    private static final String DatabaseName = "AlwaysEmptyTestDatabase";
    private static final String UserName = "postgres";
    private static final String Password = "sup3r42pass";

    public static String getDatabaseName() {
        return DatabaseName;
    }

    public static String getUserName() {
        return UserName;
    }

    public static String getPassword() {
        return Password;
    }
}
