package com.badlogic.gdx.sql;

/**
 * This public interface contains all the methods to expose results from a query on a SQLiteDatabase. This is not thread-safe.
 *
 * @author M Rafay Aleem
 */
public interface DatabaseCursor {

    /**
     * Returns the value of the requested column as a byte array.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a byte array.
     */
    byte[] getBlob(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a byte array.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a byte array.
     */
    byte[] getBlob(String columnName) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a double.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a double.
     */
    double getDouble(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a double.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a double.
     */
    double getDouble(String columnName) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a float.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a float.
     */
    float getFloat(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a float.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a float.
     */
    float getFloat(String columnName) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a int.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a int.
     */
    int getInt(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a int.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a int.
     */
    int getInt(String columnName) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a long.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a long.
     */
    long getLong(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a long.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a long.
     */
    long getLong(String columnName) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a short.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a short.
     */
    short getShort(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a short.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a short.
     */
    short getShort(String columnName) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a string.
     *
     * @param columnIndex the zero-based index of the target column.
     * @return the value of that column as a string.
     */
    String getString(int columnIndex) throws SQLiteGdxException;

    /**
     * Returns the value of the requested column as a string.
     *
     * @param columnName the name of the target column.
     * @return the value of that column as a string.
     */
    String getString(String columnName) throws SQLiteGdxException;

    /**
     * Move the cursor to the first row.
     *
     * @return whether the move was successful.
     */
    boolean first() throws SQLiteGdxException;

    /**
     * Move the cursor to the next row.
     *
     * @return whether the move was successful.
     */
    boolean next() throws SQLiteGdxException;

    /**
     * Returns the numbers of rows in the cursor.
     *
     * @return number of rows
     * @throws SQLiteGdxException
     */
    int getCount() throws SQLiteGdxException;

    /**
     * Closes the Cursor, releasing all of its resources and making it completely invalid.
     */
    void close() throws SQLiteGdxException;

}