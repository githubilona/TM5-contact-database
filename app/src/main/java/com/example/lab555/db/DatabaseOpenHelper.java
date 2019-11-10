package com.example.lab555.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.lab555.pojo.Student;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private Context mContext;
    final static String _ID = "_id";
    final static String DEBTOR_NAME = "name";
    final static String DEBTOR_PHONE = "phone";
    final static String DEBTOR_PHOTO_URI = "photo_uri";
    final static String TABLE_NAME = "debtors";
    final static String[] columns = {_ID, DEBTOR_NAME, DEBTOR_PHONE,
            DEBTOR_PHOTO_URI};
    final private static String NAME = "students_db";
    final private static Integer VERSION = 1;
    final private static String CREATE_CMD =
            "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DEBTOR_NAME + " TEXT NOT NULL, "
                    + DEBTOR_PHONE + " TEXT NOT NULL, "
                    + DEBTOR_PHOTO_URI + " TEXT );";

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;

    }
    public List<Student> readDBdata(){
        List<Student> students = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                null,null,null,null,null,_ID);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Student student = new Student();
            int idx = cursor.getColumnIndex(DEBTOR_NAME);
            student.setName(cursor.getString(idx));
            idx = cursor.getColumnIndex(DEBTOR_PHONE);
            student.setPhone(cursor.getString(idx));
            idx = cursor.getColumnIndex(DEBTOR_PHOTO_URI);
            student.setImageUri(Uri.parse(cursor.getString(idx)));
            idx = cursor.getColumnIndex(_ID);
            student.setId(Integer.parseInt(cursor.getString(idx)));
            students.add(student);
            cursor.moveToNext();
        }
        cursor.close();
        for(Student s : students){
            System.out.println("id: " + s.getId() );
            System.out.println("name: " + s.getName() );
            System.out.println("phone: " + s.getPhone() );
            System.out.println("image: " + s.getImageUri() );
        }
        return students;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long addRecord(String name, String phone, String photoUri){
        SQLiteDatabase mDB = getWritableDatabase();

        ContentValues values = new ContentValues();
       // values.put(DatabaseOpenHelper._ID,);
        values.put(DatabaseOpenHelper.DEBTOR_NAME, name);
        values.put(DatabaseOpenHelper.DEBTOR_PHONE, phone);
        values.put(DatabaseOpenHelper.DEBTOR_PHOTO_URI, photoUri);

         long id = mDB.insert(DatabaseOpenHelper.TABLE_NAME, null , values) ;
        readDBdata();
        System.out.println("NEWLY ADDED ID                     " + id );
         return id;

    }
    public boolean deleteRecord(long id){
        SQLiteDatabase sqLiteDatabase =getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, _ID +"=?",new String[]{String.valueOf(id)}) > 0;
    }
    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }

//    public static String getId() {
//        return _ID;
//    }

    public static String getDebtorName() {
        return DEBTOR_NAME;
    }

    public static String getDebtorPhone() {
        return DEBTOR_PHONE;
    }

    public static String getDebtorPhotoUri() {
        return DEBTOR_PHOTO_URI;
    }
}
