package com.fitnessprogram.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class UserDataBaseProvider extends ContentProvider {

    public static final String LOG_TAG = UserDataBaseProvider.class.getSimpleName();
    public static final boolean DEBUG = true;

    private static final String DATABASE_NAME = "fitnessuserlist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String USER_TABLE = "userProf";

    private static final String USER_ID = "_id";
    private static final String USER_NAME = "name";
    private static final String USER_WEIGHT = "weight";

    private static final String DB_CREATE = "create table " + USER_TABLE + "(" + USER_ID
            + " integer primary key autoincrement, " + USER_NAME + " text, " + USER_WEIGHT + " integer"
            + ");";

    private static final String AUTHORYTY = "com.fitnessprogram.database.Users";
    private static final String USER_PATH = "userProf";

    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORYTY + "/" + USER_PATH);

    private static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORYTY + "."
            + USER_PATH;
    private static final String USER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORYTY + "."
            + USER_PATH;

    private static final int URI_USERS = 1;
    private static final int URI_USERS_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORYTY, USER_TABLE, URI_USERS);
        uriMatcher.addURI(AUTHORYTY, USER_TABLE + "/#", URI_USERS_ID);
    }

    UserDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new UserDBHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
        case URI_USERS:
            break;
        case URI_USERS_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                selection = USER_ID + " = " + id;
            } else {
                selection += " AND " + USER_ID + " = " + id;
            }
            break;
        default:
            throw new IllegalArgumentException("Wrong uri :: delete");
        }

        db = dbHelper.getWritableDatabase();
        int count = db.delete(USER_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
        case URI_USERS:
            return USER_CONTENT_TYPE;
        case URI_USERS_ID:
            return USER_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_USERS)
            throw new IllegalArgumentException("bad insert");

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(USER_TABLE, null, values);
        Uri uriResult = ContentUris.withAppendedId(USER_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(uriResult, null);
        return uriResult;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        switch (uriMatcher.match(uri)) {
        case URI_USERS:
            if (TextUtils.isEmpty(sortOrder)) {
                sortOrder = USER_NAME + " ASC";
            }
            break;
        case URI_USERS_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                selection = USER_ID + "=" + id;
            } else {
                selection += " AND " + USER_ID + " = " + id;
            }
            break;
        default:
            throw new IllegalArgumentException("Wrong uri :: query URI == " + uri + "  match uri == "
                    + uriMatcher.match(uri));
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(USER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), USER_CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
        case URI_USERS:
            break;
        case URI_USERS_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                selection = USER_ID + " = " + id;
            } else {
                selection += " AND " + USER_ID + " = " + id;
            }
            break;
        default:
            throw new IllegalArgumentException("Wrong uri :: update");
        }

        db = dbHelper.getWritableDatabase();
        int count = db.update(USER_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private class UserDBHelper extends SQLiteOpenHelper {

        public UserDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            for (int i = 0; i < 3; i++) {
                cv.put(USER_NAME, "name" + i);
                cv.put(USER_WEIGHT, i + 100);
                db.insert(USER_TABLE, null, cv);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Stub
        }

    }
}
