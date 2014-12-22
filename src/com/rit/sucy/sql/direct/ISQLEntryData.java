package com.rit.sucy.sql.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>A container for the data from an SQL table entry.</p>
 * <p>This is to be implemented by other plugins to match their
 * data structures.</p>
 */
public interface ISQLEntryData {

    /**
     * <p>Loads the needed data from the result set.</p>
     * <p>Get the values and don't store the result set
     * anywhere as it will be closed to prevent leaks.</p>
     *
     * @param set set to load from
     * @throws java.sql.SQLException when retrieving a value from the result set goes wrong
     */
    public void loadData(ResultSet set) throws SQLException;
}
