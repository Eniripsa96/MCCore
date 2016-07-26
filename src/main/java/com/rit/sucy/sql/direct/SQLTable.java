/**
 * MCCore
 * com.rit.sucy.sql.direct.SQLTable
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 Steven Sucy
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software") to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rit.sucy.sql.direct;

import com.rit.sucy.sql.ColumnType;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>Represents a single table in a MySQL database.</p>
 * <p>If you close the MySQL connection, this will become invalid
 * as it uses prepared statements from that connection. Get the tables
 * each time you connect to the database rather than saving them.</p>
 */
public class SQLTable {

    private final HashMap<String, SQLEntry> entries = new HashMap<String, SQLEntry>();

    private final PreparedStatement
            QUERY_NAME,
            QUERY_ALL,
            CREATE_ENTRY,
            DELETE_ENTRY;

    private SQLDatabase database;
    private String name;

    /**
     * <p>Constructs a new table. This should only be called by
     * SQLDatabase when it creates a new table or retrieves an
     * already created one.</p>
     *
     * @param sql  sql connection
     * @param name name of the table
     */
    public SQLTable(SQLDatabase sql, String name) {
        this.database = sql;
        this.name = name;

        QUERY_NAME = sql.getStatement("SELECT * FROM " + name + " WHERE Name = ?");
        QUERY_ALL = sql.getStatement("SELECT * FROM " + name);
        CREATE_ENTRY = sql.getStatement("INSERT INTO " + name + " (Name) VALUES (?)");
        DELETE_ENTRY = sql.getStatement("DELETE FROM " + name + " WHERE Name=?");
    }

    /**
     * <p>Retrieves the name of the table.</p>
     *
     * @return table name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Checks whether or not a column exists in the table.</p>
     *
     * @param name name of the column
     *
     * @return true if exists, false otherwise
     */
    public boolean columnExists(String name) {

        // Check if the table exists
        try {
            DatabaseMetaData meta = database.getMeta();
            ResultSet result = meta.getColumns(null, null, this.name, name);
            boolean exists = result.next();
            result.close();
            return exists;
        }

        // An error occurred
        catch (Exception ex) {
            database.getLogger().severe("Unable to validate table: " + ex.getMessage());
        }

        return false;
    }

    /**
     * <p>Creates a new column in the table.</p>
     *
     * @param name name of the column
     * @param type type of the column
     */
    public void createColumn(String name, ColumnType type) {
        if (columnExists(name)) return;
        try {
            Statement statement = database.getStatement();
            statement.execute("ALTER TABLE " + this.name + " ADD " + name + " " + type.toString());
        } catch (Exception ex) {
            database.getLogger().severe("Failed to add the column \"" + name + "\" to the table \"" + this.name + "\" - " + ex.getMessage());
        }
    }

    /**
     * Queries the MySQL table for a specific entry
     *
     * @param name entry name
     *
     * @return query results
     */
    public ResultSet query(String name) {

        // Query the database
        try {
            QUERY_NAME.setString(1, name);
            return QUERY_NAME.executeQuery();
        }

        // Problems occurred
        catch (Exception ex) {
            database.getLogger().severe("Failed to query SQL database: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Queries the MySQL table
     *
     * @return query results
     */
    public ResultSet queryAll() {

        // Query the database
        try {
            return QUERY_ALL.executeQuery();
        }

        // Problems occurred
        catch (Exception ex) {
            database.getLogger().severe("Failed to query SQL database: " + ex.getMessage());
            return null;
        }
    }

    /**
     * <p>Loads the data from the table using the data class
     * provided.</p>
     * <p>This returns null an error occurred.</p>
     *
     * @param c   the data class to use
     * @param <T> the type of the data class
     *
     * @return the list of loaded data
     */
    public <T extends ISQLEntryData> List<T> getAllData(Class<T> c) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            ResultSet set = queryAll();
            while (set.next()) {
                T container = c.newInstance();
                container.loadData(set);
                list.add(container);
            }
            set.close();
            return list;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Checks if an entry already exists
     *
     * @param name entry name
     *
     * @return true if exists, false otherwise
     */
    public boolean entryExists(String name) {
        if (entries.containsKey(name)) return true;

        // Query for the player
        ResultSet result = query(name);

        // Try to get the next result
        if (result != null) {
            try {
                boolean exists = result.next();
                result.close();
                return exists;
            }

            // Failed to get the next result
            catch (Exception ex) {
                database.getLogger().severe("Failed to check for an existing entry: " + ex.getMessage());
            }
        }

        return false;
    }

    /**
     * <p>Creates a new entry from the table.</p>
     * <p>If the entry already exists, that will be returned instead.</p>
     *
     * @param name name of the entry
     *
     * @return entry representation
     */
    public SQLEntry createEntry(String name) {

        // Special cases
        if (!database.isConnected()) return null;
        if (entries.containsKey(name)) return entries.get(name);
        if (entryExists(name)) return new SQLEntry(database, this, name);

        // Create the table
        try {
            CREATE_ENTRY.setString(1, name);
            CREATE_ENTRY.execute();
            SQLEntry entry = new SQLEntry(database, this, name);
            entries.put(name, entry);
            database.getLogger().info("Created a new MySQL table with the name: " + name);
            return entry;
        }

        // An error occurred
        catch (Exception ex) {
            database.getLogger().severe("Failed to create entry \"" + name + "\" - " + ex.getMessage());
        }

        return null;
    }

    /**
     * <p>Deletes an entry from the table.</p>
     *
     * @param name entry name
     *
     * @return true if successful, false otherwise
     */
    public boolean deleteEntry(String name) {
        if (database.isConnected()) {
            try {
                DELETE_ENTRY.setString(1, name);
                DELETE_ENTRY.execute();
                entries.remove(name);
                return true;
            } catch (Exception ex) {
                database.getLogger().severe("Failed to delete table \"" + name + "\" - " + ex.getMessage());
            }
        }
        return false;
    }
}
