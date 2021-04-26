package com.badlogic.gdx.sqlite.robovm;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;

import SQLite.JDBCDriver;

import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;

/**
 * @author truongps
 */
public class RobovmDatabase implements Database {
    static final String TAG = "RobovmDatabase";

    /**
     * Location of database.
     */
    static final String DB_URL = "sqlite:/"
            + (new File(System.getenv("HOME"), "Library/test.db"))
            .getAbsolutePath();

    private final String dbName;
    private final int dbVersion;
    private final String dbOnCreateQuery;
    private final String dbOnUpgradeQuery;

    Connection connection;
    Statement statement;

    public RobovmDatabase(String dbName, int dbVersion, String dbOnCreateQuery, String dbOnUpgradeQuery) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        this.dbOnCreateQuery = dbOnCreateQuery;
        this.dbOnUpgradeQuery = dbOnUpgradeQuery;
    }

    @Override
    public void setupDatabase() {

    }

    @Override
    public void openOrCreateDatabase() throws SQLiteGdxException {
        JDBCDriver jdbcDriver = new JDBCDriver();
        try {
            String DB_URL = "sqlite:/"
                    + (new File(System.getenv("HOME"), "Library/" + dbName))
                    .getAbsolutePath();
            connection = jdbcDriver.connect(DB_URL, null);
            statement = connection.createStatement();
        } catch (Exception e) {
            throw new SQLiteGdxException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void closeDatabase() throws SQLiteGdxException {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new SQLiteGdxException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void execSQL(String sql) throws SQLiteGdxException {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLiteGdxException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public long executeInsert(String sql, Map<Integer, Object> values) throws SQLiteGdxException {
        return executeUpdateDelete(sql, values);
    }

    @Override
    public int executeUpdateDelete(String sql, Map<Integer, Object> values) throws SQLiteGdxException {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (values != null) {
                for (Map.Entry<Integer, Object> entry : values.entrySet()) {
                    Object value = entry.getValue();
                    if (value == null) {
                        statement.setNull(entry.getKey(), Types.NULL);
                    } else if (value instanceof String) {
                        statement.setString(entry.getKey(), (String) value);
                    } else if (value instanceof Long) {
                        statement.setLong(entry.getKey(), (Long) value);
                    } else if (value instanceof Double) {
                        statement.setDouble(entry.getKey(), (Double) value);
                    } else if (value instanceof Float) {
                        statement.setFloat(entry.getKey(), (Float) value);
                    } else if (value instanceof Boolean) {
                        statement.setBoolean(entry.getKey(), (Boolean) value);
                    } else if (value instanceof byte[]) {
                        statement.setBytes(entry.getKey(), (byte[]) value);
                    } else {
                        statement.setString(entry.getKey(), String.valueOf(value));
                    }
                }
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLiteGdxException(e);
        }
    }

    @Override
    public DatabaseCursor rawQuery(String sql) throws SQLiteGdxException {
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            RobovmCursor databaseCursor = new RobovmCursor(resultSet);
            return databaseCursor;
        } catch (SQLException e) {
            throw new SQLiteGdxException(e);
        }
    }

    @Override
    public DatabaseCursor rawQuery(DatabaseCursor cursor, String sql)
            throws SQLiteGdxException {
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            ((RobovmCursor) cursor).setNativeCursor(resultSet);
            return cursor;
        } catch (SQLException e) {
            throw new SQLiteGdxException(e);
        }
    }

}
