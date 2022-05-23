package fr.mrlaikz.spartaguild.database;

import fr.mrlaikz.spartaguild.SpartaGuild;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private SpartaGuild plugin;

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public Database(SpartaGuild plugin) {
        this.plugin = plugin;
        host = plugin.strConfig("database.host");
        port = plugin.strConfig("database.port");
        database = plugin.strConfig("database.database");
        username = plugin.strConfig("database.username");
        password = plugin.strConfig("database.password");
    }

    private Connection connection;

    public boolean isConnected() {
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false",
                username, password);
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
