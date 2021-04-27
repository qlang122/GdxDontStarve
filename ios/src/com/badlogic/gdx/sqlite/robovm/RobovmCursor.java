package com.badlogic.gdx.sqlite.robovm;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxRuntimeException;

/**
 * @author truongps
 */
public class RobovmCursor implements DatabaseCursor {
    ResultSet nativeCursor;

    public RobovmCursor() {

    }

    public RobovmCursor(ResultSet resultSet) {
        setNativeCursor(resultSet);
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        try {
            Blob blob = nativeCursor.getBlob(columnIndex + 1);
            return blob != null ? blob.getBytes(1, (int) blob.length()) : null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public byte[] getBlob(String columnName) {
        try {
            Blob blob = nativeCursor.getBlob(columnName);
            return blob != null ? blob.getBytes(0, (int) blob.length()) : null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public double getDouble(int columnIndex) {
        try {
            return nativeCursor.getDouble(columnIndex + 1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public double getDouble(String columnName) {
        try {
            return nativeCursor.getDouble(columnName);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return nativeCursor.getFloat(columnIndex + 1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public float getFloat(String columnName) {
        try {
            return nativeCursor.getFloat(columnName);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public int getInt(int columnIndex) {
        try {
            return nativeCursor.getInt(columnIndex + 1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public int getInt(String columnName) {
        try {
            return nativeCursor.getInt(columnName);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public long getLong(int columnIndex) {
        try {
            return nativeCursor.getLong(columnIndex + 1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public long getLong(String columnName) {
        try {
            return nativeCursor.getLong(columnName);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public short getShort(int columnIndex) {
        try {
            return nativeCursor.getShort(columnIndex + 1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public short getShort(String columnName) {
        try {
            return nativeCursor.getShort(columnName);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public String getString(int columnIndex) {
        try {
            return nativeCursor.getString(columnIndex + 1);
        } catch (SQLException e) {
            return "";
        }
    }

    @Override
    public String getString(String columnName) {
        try {
            return nativeCursor.getString(columnName);
        } catch (SQLException e) {
            return "";
        }
    }

    @Override
    public boolean first() {
        try {
            return nativeCursor.first();
        } catch (SQLException e) {
            Gdx.app.log(DatabaseFactory.ERROR_TAG, "There was an error in moving the cursor to first", e);
            throw new SQLiteGdxRuntimeException(e);
        }
    }

    @Override
    public boolean next() {
        try {
            return nativeCursor.next();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int getCount() {
        try {
            return nativeCursor.getRow();
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public void close() {
        try {
            nativeCursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNativeCursor(ResultSet resultSet) {
        this.nativeCursor = resultSet;
    }

}
