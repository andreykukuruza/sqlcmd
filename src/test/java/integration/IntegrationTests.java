package integration;


import config.Config;
import controller.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTests {
    private static ConfigurableInputStream in;
    private static LogOutputStream out;

    @BeforeAll
    static void setUp() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void resetData() {
        out.resetData();
    }

    @Test
    void canCreateAndDropTable() {
//        given
        in.add("connect|" + Config.getDatabaseName() + "|" + Config.getUserName() + "|" + Config.getPassword());
        in.add("create|testTable|name|text|id|int");
        in.add("tables");
        in.add("drop|testTable");
        in.add("tables");
        in.add("exit");
//        when
        Main.main(new String[0]);
//        then
        assertEquals("Hello! It is SQLCmd! Enter command or help:\r\n" +
                "Connect is successful. Enter next command or help:\r\n" +
                "Table testTable was created. Enter next command or help:\r\n" +
                "[testtable]\r\n" +
                "Enter next command or help:\r\n" +
                "Table testTable was dropped. Enter next command or help:\r\n" +
                "No tables inside the database.\r\n" +
                "Enter next command or help:\r\n" +
                "Goodbye!\r\n", out.getData());
    }

    @Test
    void canCreateInsertUpdateDeletePrintDrop() {
//        given
        in.add("connect|" + Config.getDatabaseName() + "|" + Config.getUserName() + "|" + Config.getPassword());
        in.add("create|users|name|text|id|int");
        in.add("tables");
        in.add("insert|users|name|'Stiven'|id|1");
        in.add("insert|users|name|'Eva'|id|2");
        in.add("find|users");
        in.add("update|users|id|2|name|'Jessika'");
        in.add("delete|users|id|1");
        in.add("drop|users");
        in.add("tables");
        in.add("exit");
//        when
        Main.main(new String[0]);
//        then
        assertEquals("Hello! It is SQLCmd! Enter command or help:\r\n" +
                "Connect is successful. Enter next command or help:\r\n" +
                "Table users was created. Enter next command or help:\r\n" +
                "[users]\r\n" +
                "Enter next command or help:\r\n" +
                "Data was successful insert in the table.\r\n" +
                "Enter next command or help:\r\n" +
                "Data was successful insert in the table.\r\n" +
                "Enter next command or help:\r\n" +
                "+------+--+\n" +
                "|name  |id|\n" +
                "+------+--+\n" +
                "|Stiven|1 |\n" +
                "|Eva   |2 |\n" +
                "+------+--+\r\n" +
                "Enter next command or help:\r\n" +
                "+-------+--+\n" +
                "|name   |id|\n" +
                "+-------+--+\n" +
                "|Stiven |1 |\n" +
                "|Jessika|2 |\n" +
                "+-------+--+\r\n" +
                "Enter next command or help:\r\n" +
                "+-------+--+\n" +
                "|name   |id|\n" +
                "+-------+--+\n" +
                "|Jessika|2 |\n" +
                "+-------+--+\r\n" +
                "Enter next command or help:\r\n" +
                "Table users was dropped. Enter next command or help:\r\n" +
                "No tables inside the database.\r\n" +
                "Enter next command or help:\r\n" +
                "Goodbye!\r\n", out.getData());
    }

    @Test
    void canCreateInsertDrop() {
//        given
        in.add("connect|" + Config.getDatabaseName() + "|" + Config.getUserName() + "|" + Config.getPassword());
        in.add("create|users|name|text|id|int");
        in.add("tables");
        in.add("insert|users|name|'Stiven'|id|1");
        in.add("insert|users|name|'Eva'|id|2");
        in.add("drop|users");
        in.add("tables");
        in.add("exit");
//        when
        Main.main(new String[0]);
//        then
        assertEquals("Hello! It is SQLCmd! Enter command or help:\r\n" +
                "Connect is successful. Enter next command or help:\r\n" +
                "Table users was created. Enter next command or help:\r\n" +
                "[users]\r\n" +
                "Enter next command or help:\r\n" +
                "Data was successful insert in the table.\r\n" +
                "Enter next command or help:\r\n" +
                "Data was successful insert in the table.\r\n" +
                "Enter next command or help:\r\n" +
                "Table users was dropped. Enter next command or help:\r\n" +
                "No tables inside the database.\r\n" +
                "Enter next command or help:\r\n" +
                "Goodbye!\r\n", out.getData());
    }

    @Test
    void canCreateInsertClearPrintDropTable() {
//        given
        in.add("connect|" + Config.getDatabaseName() + "|" + Config.getUserName() + "|" + Config.getPassword());
        in.add("create|users|name|text|id|int");
        in.add("tables");
        in.add("insert|users|name|'Stiven'|id|1");
        in.add("insert|users|name|'Eva'|id|2");
        in.add("find|users");
        in.add("clear|users");
        in.add("find|users");
        in.add("drop|users");
        in.add("tables");
        in.add("exit");
//        when
        Main.main(new String[0]);
//        then
        assertEquals("Hello! It is SQLCmd! Enter command or help:\r\n" +
                "Connect is successful. Enter next command or help:\r\n" +
                "Table users was created. Enter next command or help:\r\n" +
                "[users]\r\n" +
                "Enter next command or help:\r\n" +
                "Data was successful insert in the table.\r\n" +
                "Enter next command or help:\r\n" +
                "Data was successful insert in the table.\r\n" +
                "Enter next command or help:\r\n" +
                "+------+--+\n" +
                "|name  |id|\n" +
                "+------+--+\n" +
                "|Stiven|1 |\n" +
                "|Eva   |2 |\n" +
                "+------+--+\r\n" +
                "Enter next command or help:\r\n" +
                "Table users was cleared. Enter next command or help:\r\n" +
                "+----+--+\n" +
                "|name|id|\n" +
                "+----+--+\r\n" +
                "Enter next command or help:\r\n" +
                "Table users was dropped. Enter next command or help:\r\n" +
                "No tables inside the database.\r\n" +
                "Enter next command or help:\r\n" +
                "Goodbye!\r\n", out.getData());
    }
}
