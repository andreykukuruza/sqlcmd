package model;

/**
 * To successfully pass the tests, you need to create an empty database.
 * In the fields indicate the name of this database, login and password.
 **/

class Config {
    private static final String correctDatabaseName = "AlwaysEmptyTestDatabase";
    private static final String correctUserName = "postgres";
    private static final String correctPassword = "sup3r42pass";

    static String getCorrectUserName() {
        return correctUserName;
    }

    static String getCorrectPassword() {
        return correctPassword;
    }

    static String getCorrectDatabaseName() {
        return correctDatabaseName;
    }
}
