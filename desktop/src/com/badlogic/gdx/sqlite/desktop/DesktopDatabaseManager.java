package com.badlogic.gdx.sqlite.desktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.DatabaseManager;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * @author M Rafay Aleem
 */
public class DesktopDatabaseManager implements DatabaseManager {

    private class DesktopDatabase implements Database {
        private SQLiteDatabaseHelper helper = null;

        private final String dbName;
        private final int dbVersion;
        private final String dbOnCreateQuery;
        private final String dbOnUpgradeQuery;

        private Connection connection = null;
        private Statement stmt = null;

        private DesktopDatabase(String dbName, int dbVersion, String dbOnCreateQuery, String dbOnUpgradeQuery) {
            this.dbName = dbName;
            this.dbVersion = dbVersion;
            this.dbOnCreateQuery = dbOnCreateQuery;
            this.dbOnUpgradeQuery = dbOnUpgradeQuery;
        }

        @Override
        public void setupDatabase() {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                Gdx.app.log(DatabaseFactory.ERROR_TAG,
                        "Unable to load the SQLite JDBC driver. Their might be a problem with your build path or project setup.", e);
                throw new GdxRuntimeException(e);
            }
        }

        @Override
        public void openOrCreateDatabase() throws SQLiteGdxException {
            if (helper == null)
                helper = new SQLiteDatabaseHelper(dbName, dbVersion, dbOnCreateQuery, dbOnUpgradeQuery);

            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
                stmt = connection.createStatement();
                helper.onCreate(stmt);
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void closeDatabase() throws SQLiteGdxException {
            try {
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void execSQL(String sql) throws SQLiteGdxException {
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
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
            DesktopCursor lCursor = new DesktopCursor();
            try {
                ResultSet resultSetRef = stmt.executeQuery(sql);
                lCursor.setNativeCursor(resultSetRef);
                return lCursor;
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public DatabaseCursor rawQuery(DatabaseCursor cursor, String sql) throws SQLiteGdxException {
            DesktopCursor lCursor = (DesktopCursor) cursor;
            try {
                ResultSet resultSetRef = stmt.executeQuery(sql);
                lCursor.setNativeCursor(resultSetRef);
                return lCursor;
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

    }

    @Override
    public Database getNewDatabase(String dbName, int dbVersion, String dbOnCreateQuery, String dbOnUpgradeQuery) {
        return new DesktopDatabase(dbName, dbVersion, dbOnCreateQuery, dbOnUpgradeQuery);
    }

}
