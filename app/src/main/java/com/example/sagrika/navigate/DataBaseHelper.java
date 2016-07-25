package com.example.sagrika.navigate;

//This maintains an SQLite database in the app for maintaining Login and Logout Session
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "newdb";
    private static final String TB_NAME = "userinfo";
    private static final String CREATE_TABLE="CREATE TABLE userinfo(username TEXT,password TEXT)";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private Context context;

    public SQLiteDatabase db;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context=context;
        Log.e("database    ","database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        this.db = db;
        Log.e("table     ","table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TB_NAME);
        this.onCreate(db);
    }

    //Manager name and password are obtained here from Tracking Page for maintaining LoggedIn session
    public void  putData(String user,String pass){
        db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",user);
        contentValues.put("password",pass);
        db.insert(TB_NAME,null,contentValues);
        Log.e("Data     ","Data inserted");
    }

    //Called to get data from SQLite database if any manager is logged in
    public Cursor getData(){
        db =this.getReadableDatabase();
        Cursor cursor;
        String[] projections = {USERNAME,PASSWORD};
        cursor = db.query(TB_NAME,projections,null,null,null,null,null);
        return cursor;
    }

    //Called on Logout and deletes manager's info
    public void delData(){
        db= this.getWritableDatabase();
        db.delete(TB_NAME,null,null);
        Log.e("Data     ","Data deleted");
    }


}
