package model;

/**
 * To successfully pass the tests, you need to create an empty database.
 * In the fields indicate the name of this database, login and password.
 **/

class Config {
    private static final String DatabaseName = "AlwaysEmptyTestDatabase";
    private static final String UserName = "postgres";
    private static final String Password = "sup3r42pass";

    static String getUserName() {
        return UserName;
    }

    static String getPassword() {
        return Password;
    }

    static String getDatabaseName() {
        return DatabaseName;
    }
}
