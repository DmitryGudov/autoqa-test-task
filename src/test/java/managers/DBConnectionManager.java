package managers;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionManager {

    public static Session sessionSsh;
    public static Connection dbConnection;

    private static void sshTunnel() {
        if (sessionSsh == null) {
            try {
                var jsch = new JSch();
                jsch.addIdentity("/home/dev/.ssh/id_rsa", new File("/home/dev/.ssh/id_rsa").toPath().toFile().getAbsolutePath());
                sessionSsh = jsch.getSession(ConfigManager.getProperty("sshUser"), ConfigManager.getProperty("sshHost"),
                        Integer.parseInt(ConfigManager.getProperty("sshPort")));
                var sessionConfig = new Properties();
                sessionConfig.put("StrictHostKeyChecking", "no");
                sessionSsh.setConfig(sessionConfig);
                sessionSsh.connect();
                sessionSsh.setPortForwardingL(5432, "localhost", 5432);
            } catch (Exception e) {
                throw new RuntimeException("Unable to create tunnel session for db1.", e);
            }
        }
    }

    private static void setDatabaseConnection(String dbUrl, String dbLogin, String dbPassword) {
        if (dbConnection == null) {
            try {
                dbConnection = DriverManager.getConnection(dbUrl, dbLogin, dbPassword);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T databaseConnection(
            String sqlQuery
    ) {
        disconnect();
        sshTunnel();
        setDatabaseConnection(ConfigManager.getProperty("dbUrl"),
                ConfigManager.getProperty("dbLogin"),
                ConfigManager.getProperty("dbPassword"));
        return executeQuery(sqlQuery);
    }

    public static <T> T executeQuery(String sql) {
        try {
            return (T) dbConnection.prepareStatement(sql).executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Exception occurred during database query.");
    }

    public static void disconnect() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
                dbConnection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (sessionSsh != null) {
            sessionSsh.disconnect();
            sessionSsh = null;
        }
    }

}
