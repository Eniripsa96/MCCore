package com.rit.sucy.sql.direct;

import com.rit.sucy.sql.ColumnType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manager for connection to and interacting with a MySQL database
 */
public class SQLDatabase
{

    private final HashMap<String, SQLTable> tables = new HashMap<String, SQLTable>();

    private final Plugin plugin;
    private final String connectionURL;
    private final String user;
    private final String password;

    private Connection connection;

    /**
     * Initializes the data to connect to a MySQL database
     *
     * @param plugin   plugin reference
     * @param host     host name
     * @param port     port number
     * @param database database name
     * @param username username
     * @param password password
     */
    public SQLDatabase(Plugin plugin, String host, String port, String database, String username, String password)
    {
        this.plugin = plugin;
        this.connectionURL = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.user = username;
        this.password = password;
    }

    /**
     * <p>Loads SQL database data from the configuration section.</p>
     * <p>The format must be the following (not necessarily in the same
     * order) for it to be recognized:</p>
     * <code>
     * host: example
     * port: 12345
     * databse: someDatabase
     * username: myUser
     * password: myPassword
     * </code>
     *
     * @param plugin plugin reference
     * @param config configuration to loaf from
     */
    public SQLDatabase(Plugin plugin, ConfigurationSection config)
    {
        this.plugin = plugin;
        this.connectionURL = "jdbc:mysql://" + config.getString("host") + ":" + config.getString("port") + "/" + config.getString("database");
        this.user = config.getString("username");
        this.password = config.getString("password");
    }

    /**
     * <p>Gets the logger from the owning plugin.</p>
     *
     * @return logger of the owning plugin
     */
    public Logger getLogger()
    {
        return plugin.getLogger();
    }

    /**
     * <p>Retrieves the metadata of the connection.</p>
     *
     * @return metadata of the active connection
     *
     * @throws SQLException
     */
    public DatabaseMetaData getMeta() throws SQLException
    {
        return isConnected() ? connection.getMetaData() : null;
    }

    /**
     * <p>Checks whether or not the MySQL setup is connected.</p>
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected()
    {
        return connection != null;
    }

    /**
     * Opens a connection to the server
     *
     * @return true if connected successfully, false otherwise
     */
    public boolean openConnection()
    {

        // Connect to the server
        try
        {
            plugin.getLogger().info("Connecting to MySQL database...");
            connection = DriverManager.getConnection(connectionURL, this.user, this.password);
            plugin.getLogger().info("Connected to the MySQL database successfully");
        }

        // Unable to connect to the server
        catch (Exception ex)
        {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to the MySQL server: " + ex.getMessage());
        }

        return connection != null;
    }

    /**
     * Closes the connection to the database
     */
    public void closeConnection()
    {

        // Must have a connection to close it
        if (connection != null)
        {

            // Close the database connection
            try
            {
                plugin.getLogger().info("Closing connection to the MySQL database...");
                connection.close();
                connection = null;
                plugin.getLogger().info("The connection to the MySQL database has been closed!");
            }

            // Unable to close the database connection
            catch (Exception ex)
            {
                plugin.getLogger().severe("Could not close the MySQL connection: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * <p>Prepares a statement for the connection.</p>
     *
     * @param sql SQL for the statement
     *
     * @return prepared statement or null if not connected or failed to prepare it
     */
    public PreparedStatement getStatement(String sql)
    {
        try
        {
            return isConnected() ? connection.prepareStatement(sql) : null;
        }
        catch (Exception ex)
        {
            plugin.getLogger().severe("Failed to prepare an SQL statement");
            return null;
        }
    }

    /**
     * <p>Retrieves an empty statement from the connection to use.</p>
     *
     * @return empty statement
     */
    public Statement getStatement()
    {
        try
        {
            return connection.createStatement();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Checks if a table with the name exists
     *
     * @param name table name
     *
     * @return true if exists, false otherwise
     */
    public boolean tableExists(String name)
    {
        return tableExists(plugin, name);
    }

    /**
     * Checks if a table with the name exists for a plugin
     *
     * @param plugin plugin to check for
     * @param name   table name
     *
     * @return true if exists, false otherwise
     */
    public boolean tableExists(Plugin plugin, String name)
    {
        name = plugin.getName() + "_" + name;
        if (tables.containsKey(name)) return true;

        // Check if the table exists
        try
        {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet result = meta.getTables(null, null, name, null);
            boolean exists = result.next();
            result.close();
            return exists;
        }

        // An error occurred
        catch (Exception ex)
        {
            plugin.getLogger().severe("Unable to validate table: " + ex.getMessage());
        }

        return false;
    }

    /**
     * <p>Creates a new table in the database.</p>
     * <p>If the table already exists, that table will be returned instead.</p>
     *
     * @param name name of the table
     */
    public SQLTable createTable(String name)
    {
        return createTable(plugin, name);
    }

    /**
     * <p>Creates a new table in the database for a specific plugin.</p>
     * <p>If the table already exists, that table will be returned instead.</p>
     *
     * @param plugin plugin to create it for
     * @param name   name of the table
     */
    public SQLTable createTable(Plugin plugin, String name)
    {

        // Make the name plugin-specific to prevent collisions
        String full = plugin.getName() + "_" + name;

        // Special cases
        if (!isConnected())
        {
            plugin.getLogger().severe("A plugin tried to create a table while not connected to the SQL database");
            return null;
        }
        if (tables.containsKey(full)) return tables.get(full);
        if (tableExists(plugin, name))
        {
            SQLTable table = new SQLTable(this, full);
            tables.put(full, table);
            return table;
        }

        // Create the table
        try
        {
            getStatement().execute("CREATE TABLE " + full + " (Name " + ColumnType.STRING_64.toString() + ")");
            SQLTable table = new SQLTable(this, full);
            tables.put(full, table);
            plugin.getLogger().info("Created a new MySQL table with the name: " + full);
            return table;
        }

        // An error occurred
        catch (Exception ex)
        {
            plugin.getLogger().severe("Failed to create table \"" + name + "\" - " + ex.getMessage());
        }

        return null;
    }

    /**
     * <p>Deletes a table from the database.</p>
     *
     * @param name name of the table to delete
     *
     * @return true if successful, false otherwise
     */
    public boolean deleteTable(String name)
    {
        return deleteTable(plugin, name);
    }

    /**
     * <p>Deletes a table from the database for a plugin.</p>
     *
     * @param plugin plugin to delete the table for
     * @param name   name of the table to delete
     *
     * @return true if successful, false otherwise
     */
    public boolean deleteTable(Plugin plugin, String name)
    {
        if (isConnected() && tableExists(plugin, name))
        {
            try
            {
                String full = plugin.getName() + "_" + name;
                getStatement().execute("DROP TABLE " + full);
                tables.remove(full);
                return true;
            }
            catch (Exception ex)
            {
                plugin.getLogger().severe("Failed to delete table \"" + name + "\" - " + ex.getMessage());
            }
        }
        return false;
    }
}
