package com.badlogic.gdx.sql;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * This public interface contains the necessary methods to setup and execute queries on a database. The factory method
 * {@link DatabaseFactory#getNewDatabase(String, int, String, String)} will return a database object that implements this
 * interface. The typical sequence of method calls should be as follows:
 * <ul>
 * <li>{@link Database#setupDatabase()}</li>
 * <li>{@link Database#openOrCreateDatabase()}</li>
 * <li>{@link Database#execSQL(String)} OR</li>
 * <li>{@link Database#rawQuery(String)} OR</li>
 * <li>{@link Database#rawQuery(DatabaseCursor, String)}</li>
 * <li>{@link Database#closeDatabase()}</li>
 * </ul>
 *
 * @author M Rafay Aleem
 */
public interface Database {

    /**
     * This method is needed to be called only once before any database related activity can be performed. The method performs the
     * necessary procedures for the database. However, a database will not be opened/created until
     * {@link Database#openOrCreateDatabase()} is called.
     */
    void setupDatabase();

    /**
     * Opens an already existing database or creates a new database if it doesn't already exist.
     *
     * @throws SQLiteGdxException
     */
    void openOrCreateDatabase() throws SQLiteGdxException;

    /**
     * Closes the opened database and releases all the resources related to this database.
     *
     * @throws SQLiteGdxException
     */
    void closeDatabase() throws SQLiteGdxException;

    /**
     * Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
     *
     * @param sql the SQL statement to be executed. Multiple statements separated by semicolons are not supported.
     * @throws SQLiteGdxException
     */
    void execSQL(@NotNull String sql) throws SQLiteGdxException;

    /**
     * Executes the SQL statement and return the ID of the row inserted due to this call.
     * in android only <code>INSERT</code>, in others, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     *
     * @param sql    the SQL statement to be executed. Multiple statements separated by semicolons are not supported.
     * @param values A list of which columns include ?s in selection, which will be replaced by the values, key should be index.
     * @return the row ID of the last row inserted, if this insert is successful. -1 otherwise.
     * @throws SQLiteGdxException
     */
    long executeInsert(@NotNull String sql, Map<Integer, Object> values) throws SQLiteGdxException;

    /**
     * Executes the SQL statement in this <code>PreparedStatement</code> object,
     * which must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     *
     * @param sql    the SQL statement to be executed. Multiple statements separated by semicolons are not supported.
     * @param values A list of which columns include ?s in selection, which will be replaced by the values, key should be index.
     * @return the number of rows affected by this SQL statement execution.
     * @throws SQLiteGdxException
     */
    int executeUpdateDelete(@NotNull String sql, Map<Integer, Object> values) throws SQLiteGdxException;

    /**
     * Runs the provided SQL and returns a {@link DatabaseCursor} over the result set.
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @return {@link DatabaseCursor}
     * @throws SQLiteGdxException
     */
    DatabaseCursor rawQuery(@NotNull String sql) throws SQLiteGdxException;

    /**
     * Runs the provided SQL and returns a {@link DatabaseCursor} over the result set.
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @return {@link DatabaseCursor}
     * @throws SQLiteGdxException
     */
    DatabaseCursor rawQuery(@NotNull String sql, String[] selectionArgs) throws SQLiteGdxException;

    /**
     * Runs the provided SQL and returns the same {@link DatabaseCursor} that was passed to this method. Use this method when you
     * want to avoid reallocation of {@link DatabaseCursor} object. Note that you shall only pass the {@link DatabaseCursor} object
     * that was previously returned by a rawQuery method. Creating your own {@link DatabaseCursor} and then passing it as an object
     * will not work.
     *
     * @param cursor existing {@link DatabaseCursor} object
     * @param sql    the SQL query. The SQL string must not be ; terminated
     * @return the passed {@link DatabaseCursor}.
     * @throws SQLiteGdxException
     */
    DatabaseCursor rawQuery(@NotNull DatabaseCursor cursor, @NotNull String sql) throws SQLiteGdxException;

}
