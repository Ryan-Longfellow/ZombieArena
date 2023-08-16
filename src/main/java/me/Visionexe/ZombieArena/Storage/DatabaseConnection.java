package me.Visionexe.ZombieArena.Storage;

import me.Visionexe.ZombieArena.ZombieArena;

import java.io.File;
import java.sql.*;
import java.util.Optional;

public class DatabaseConnection {

    private Connection connection;
    public DatabaseConnection() {
        connect();
    }

    /**
     * Creates connection to database
     */
    public void connect() {
        try {
            String database = ZombieArena.getInstance().getFlatfile().get(ZombieArena.FILE_SETTINGS).getString("database", "data.db");
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + ZombieArena.getInstance().getDataFolder().getPath() + File.separator + database);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares statement for given Query
     * @param query
     * @return
     */
    public Optional<PreparedStatement> prepareStatement(String query) {
        PreparedStatement prepStatement = null;
        try {
            prepStatement = this.connection.prepareStatement(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(prepStatement);
    }

    /**
     * Executes update states. Used for statements that do not return data (INSERT/UPDATE)
     * @param prepStatement
     */
    public void update(PreparedStatement prepStatement) {
        try {
            prepStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {
                prepStatement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Executes a query statement. Used for queries that return data (SELECT/COUNT)
     * @param prepStatement
     * @return
     */
    public ResultSet query(PreparedStatement prepStatement) {
        ResultSet result = null;
        try {
            result = prepStatement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    /**
     * Close database connection
     */
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
