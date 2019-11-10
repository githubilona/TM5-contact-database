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
    final static String STUDENT_NAME = "name";
    final static String STUDENT_PHONE = "phone";
    final static String STUDENT_PHOTO_URI = "photo_uri";
    final static String TABLE_NAME = "students";
    final static String[] columns = {_ID, STUDENT_NAME, STUDENT_PHONE,
            STUDENT_PHOTO_URI};
    final private static String NAME = "students_db";
    final private static Integer VERSION = 1;
    final private static String CREATE_CMD =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + STUDENT_NAME + " TEXT NOT NULL, "
                    + STUDENT_PHONE + " TEXT NOT NULL, "
                    + STUDENT_PHOTO_URI + " TEXT );";

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    public List<Student> readDBdata() {
        List<Student> students = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                null, null, null, null, null, _ID);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Student student = new Student();
            int idx = cursor.getColumnIndex(STUDENT_NAME);
            student.setName(cursor.getString(idx));
            idx = cursor.getColumnIndex(STUDENT_PHONE);
            student.setPhone(cursor.getString(idx));
            idx = cursor.getColumnIndex(STUDENT_PHOTO_URI);
            student.setImageUri(Uri.parse(cursor.getString(idx)));
            idx = cursor.getColumnIndex(_ID);
            student.setId(Integer.parseInt(cursor.getString(idx)));
            students.add(student);
            cursor.moveToNext();
        }
        cursor.close();
        for (Student s : students) {
            System.out.println("id: " + s.getId());
            System.out.println("name: " + s.getName());
            System.out.println("phone: " + s.getPhone());
            System.out.println("image: " + s.getImageUri());
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

    public long addRecord(String name, String phone, String photoUri) {
        SQLiteDatabase mDB = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.STUDENT_NAME, name);
        values.put(DatabaseOpenHelper.STUDENT_PHONE, phone);
        values.put(DatabaseOpenHelper.STUDENT_PHOTO_URI, photoUri);

        long id = mDB.insert(DatabaseOpenHelper.TABLE_NAME, null, values);
        readDBdata();
        System.out.println("NEWLY ADDED ID                     " + id);
        return id;
    }

    public boolean deleteRecord(long id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, _ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }

    public static String getId() {
        return _ID;
    }

    public static String getStudentName() {
        return STUDENT_NAME;
    }

    public static String getStudentPhone() {
        return STUDENT_PHONE;
    }

    public static String getStudentPhotoUri() {
        return STUDENT_PHOTO_URI;
    }
}
