package db;

import oracle.jdbc.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    static Connection connection = null;
    static final String connectionUrl = """
                jdbc:oracle:thin:@10.10.108.136:1521/xe
                """;
    public static void driverLoad() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println(OracleDriver.getDriverVersion());
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패");
        }
    }
    public static Connection getConnection() throws SQLException  {
        connection = DriverManager.getConnection(connectionUrl, "c##seungho", "123456");
        return connection;
    }
}
