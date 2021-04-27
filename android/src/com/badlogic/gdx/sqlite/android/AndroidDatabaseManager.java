package com.badlogic.gdx.sqlite.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseManager;
import com.badlogic.gdx.sql.SQLiteGdxException;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author M Rafay Aleem
 */
public class AndroidDatabaseManager implements DatabaseManager {

    private Context context;

    private class AndroidDatabase implements Database {
        private SQLiteDatabaseHelper helper;
        private SQLiteDatabase database;
        private Context context;

        private final String dbName;
        private final int dbVersion;
        private final String dbOnCreateQuery;
        private final String dbOnUpgradeQuery;

        private AndroidDatabase(Context context, String dbName, int dbVersion, String dbOnCreateQuery, String dbOnUpgradeQuery) {
            this.context = context;
            this.dbName = dbName;
            this.dbVersion = dbVersion;
            this.dbOnCreateQuery = dbOnCreateQuery;
            this.dbOnUpgradeQuery = dbOnUpgradeQuery;
        }

        @Override
        public void setupDatabase() {
            helper = new SQLiteDatabaseHelper(this.context, dbName, null, dbVersion, dbOnCreateQuery, dbOnUpgradeQuery);
        }

        @Override
        public void openOrCreateDatabase() throws SQLiteGdxException {
            try {
                database = helper.getWritableDatabase();
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void closeDatabase() throws SQLiteGdxException {
            try {
                helper.close();
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public void execSQL(@NotNull String sql) throws SQLiteGdxException {
            try {
                database.execSQL(sql);
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public long executeInsert(@NotNull String sql, Map<Integer, Object> values) throws SQLiteGdxException {
            try {
                SQLiteStatement statement = database.compileStatement(sql);
                if (values != null) {
                    for (Map.Entry<Integer, Object> entry : values.entrySet()) {
                        Object value = entry.getValue();
                        if (value == null) {
                            statement.bindNull(entry.getKey());
                        } else if (value instanceof String) {
                            statement.bindString(entry.getKey(), (String) value);
                        } else if (value instanceof Long) {
                            statement.bindLong(entry.getKey(), (Long) value);
                        } else if (value instanceof Double) {
                            statement.bindDouble(entry.getKey(), (Double) value);
                        } else if (value instanceof byte[]) {
                            statement.bindBlob(entry.getKey(), (byte[]) value);
                        } else {
                            statement.bindString(entry.getKey(), String.valueOf(value));
                        }
                    }
                }
                return statement.executeInsert();
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public int executeUpdateDelete(@NotNull String sql, Map<Integer, Object> values) throws SQLiteGdxException {
            try {
                SQLiteStatement statement = database.compileStatement(sql);
                if (values != null) {
                    for (Map.Entry<Integer, Object> entry : values.entrySet()) {
                        Object value = entry.getValue();
                        if (value == null) {
                            statement.bindNull(entry.getKey());
                        } else if (value instanceof String) {
                            statement.bindString(entry.getKey(), (String) value);
                        } else if (value instanceof Long) {
                            statement.bindLong(entry.getKey(), (Long) value);
                        } else if (value instanceof Double) {
                            statement.bindDouble(entry.getKey(), (Double) value);
                        } else if (value instanceof byte[]) {
                            statement.bindBlob(entry.getKey(), (byte[]) value);
                        } else {
                            statement.bindString(entry.getKey(), String.valueOf(value));
                        }
                    }
                }
                return statement.executeUpdateDelete();
            } catch (SQLException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public DatabaseCursor rawQuery(@NotNull String sql) throws SQLiteGdxException {
            AndroidCursor aCursor = new AndroidCursor();
            try {
                Cursor tmp = database.rawQuery(sql, null);
                aCursor.setNativeCursor(tmp);
                return aCursor;
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public DatabaseCursor rawQuery(@NotNull String sql, String[] selectionArgs) throws SQLiteGdxException {
            AndroidCursor aCursor = new AndroidCursor();
            try {
                Cursor tmp = database.rawQuery(sql, selectionArgs);
                aCursor.setNativeCursor(tmp);
                return aCursor;
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

        @Override
        public DatabaseCursor rawQuery(@NotNull DatabaseCursor cursor, @NotNull String sql) throws SQLiteGdxException {
            AndroidCursor aCursor = (AndroidCursor) cursor;
            try {
                Cursor tmp = database.rawQuery(sql, null);
                aCursor.setNativeCursor(tmp);
                return aCursor;
            } catch (SQLiteException e) {
                throw new SQLiteGdxException(e);
            }
        }

    }

    public AndroidDatabaseManager() {
        AndroidApplication app = (AndroidApplication) Gdx.app;
        context = app.getApplicationContext();
    }

    @Override
    public Database getNewDatabase(String databaseName, int databaseVersion, String databaseCreateQuery, String dbOnUpgradeQuery) {
        return new AndroidDatabase(this.context, databaseName, databaseVersion, databaseCreateQuery, dbOnUpgradeQuery);
    }

}
