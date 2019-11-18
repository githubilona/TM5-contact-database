package com.example.lab555.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab555.LayoutType;
import com.example.lab555.R;
import com.example.lab555.SparseBooleanArrayParcelable;
import com.example.lab555.db.DatabaseOpenHelper;
import com.example.lab555.model.Student;
import com.example.lab555.adapters.StudentsAdapter;
import com.example.lab555.tasks.AddStudentTask;
import com.example.lab555.tasks.LoadJsonTask;
import com.example.lab555.tasks.MyJsonResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentsListActivity extends AppCompatActivity {

    Button addButton;
    Button removeButton;
    Button deleteButton;
    Button cancelButton;
    ListView listView;
    List<Student> students;
    StudentsAdapter adapter;
    int index = 0;
    SparseBooleanArray checkedItemsPositions;
    int itemCount;
    LayoutType layoutType;
    boolean[] checked;
    String token;

    DatabaseOpenHelper mDbHelper;


    public void addStudent(Student student) {
        students.add(student);
        index++;
        adapter.notifyDataSetChanged();
    }

    private void fillArray() {
        checkedItemsPositions = listView.getCheckedItemPositions();
        itemCount = listView.getCount();
        checked = adapter.getChecked();

        for (int i = 0; i < itemCount; i++) {
            checkedItemsPositions.put(i, checked[i]);
        }
    }

    public void deleteOnClick(View view) {
        fillArray();

        for (int i = itemCount - 1; i >= 0; i--) {
            if (checkedItemsPositions.get(i)) {
                checked[i] = false;

                long id = students.get(i).getId();
                // W bazie kazdy student ma przypiane id w momencie dodawanie nowego rekordu.
                // Powyzej medtoda getId() pobiera id z obiektu Student a nie id przypisane automatycznie w bazie danych.
                // Gdy dodajemy studenta do lokalnej listy to trzeba przypisać jawnie w kodzie, studeci zapisywani do bazy mają
                // id przypisywane automatycznie w klasie DatabaseOpenHelper
                mDbHelper.deleteRecord(id);
                adapter.remove(i);
            }
        }
        //checkedItemsPositions.clear(); don't work
        adapter.notifyDataSetChanged();

    }

    public void cancelOnClick(View view) {
        setSimpleListView();
    }

    public void removeOnClick(View view) {
        setMultipleChoiceView();
    }

    public void addStudentREST(Long id, String name, String phone, Uri photoUri){
//        LoadJsonTask loadJsonTask = new LoadJsonTask(new MyJsonResponseListener() {
//            @Override
//            public void onJsonResponseChange(String string) {
//                System.out.println("RESPONSE ADD " + string);
//
//            }
//        });
//        loadJsonTask.execute("http://apps.ii.uph.edu.pl:88/MSK/MSK/AddDebtor?token="+token+"&dId=____1___&dName=___2__&dPhone=9999");

        AddStudentTask addStudentTask = new AddStudentTask();
//        addStudentTask.execute("http://apps.ii.uph.edu.pl:88/MSK/MSK/AddDebtor?" +
//                        "token=79e52052-3b9a-41e8-a06b-1b767cb29dce" +
//                        "&dId=.......0000000000000000...&dName=.00000000000..&dPhone=.000000000000000000..");
        try {
            addStudentTask.execute("http://apps.ii.uph.edu.pl:88/MSK/MSK/AddDebtor",
                    "token=" + URLEncoder.encode(token, "UTF-8") ,
                    "dId=" + URLEncoder.encode(id+"", "UTF-8"),
                    "dName=" +URLEncoder.encode(name, "UTF-8"),
                    "dPhone=" +URLEncoder.encode(phone, "UTF-8"));
                  //  "dUri=" +URLEncoder.encode(photoUri+"", "UTF-8")); //serwer nie zapisuje zdjęć
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                CharSequence resultName = data.getCharSequenceExtra("resultName");
                CharSequence resultPhone = data.getCharSequenceExtra("resultPhone");
                Uri photoUri = data.getParcelableExtra("photoUri");

                Long id = mDbHelper.addRecord(resultName + "", resultPhone + "", photoUri + "");
                addStudent(new Student(id, resultName + "", resultPhone + "", photoUri));
                addStudentREST(id,resultName +"",resultPhone+"",photoUri);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Name and phone can't be empty!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setSimpleListView() {
        removeButton.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);

        adapter = new StudentsAdapter(this, R.layout.student_list_item, students);
        listView.setAdapter(adapter);
        layoutType = LayoutType.SIMPLE_LAYOUT;
    }

    public void setMultipleChoiceView() {
        removeButton.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);

        adapter = new StudentsAdapter(this, R.layout.student_list_item_multiple_choice, students);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        layoutType = LayoutType.MULTIPLE_CHOICE_LAYOUT;
        adapter.notifyDataSetChanged();
    }

    public void startAddStudentActivity(View view) {
        Intent intent = new Intent(this, AddStudentActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        //  savedInstanceState.putStringArrayList("MyStrings", (ArrayList<String>) myStrings);
        savedInstanceState.putParcelableArrayList("Students", (ArrayList<? extends Parcelable>) students);
        savedInstanceState.putInt("Index", index);
        savedInstanceState.putSerializable("LayoutType", layoutType);

        checkedItemsPositions = listView.getCheckedItemPositions();
        itemCount = listView.getCount();

        if (checkedItemsPositions != null) {
            fillArray();
            savedInstanceState.putParcelable("CheckedItemPositions", new SparseBooleanArrayParcelable(checkedItemsPositions));
            savedInstanceState.putInt("ItemCount", itemCount);
            savedInstanceState.putBooleanArray("Checked", checked);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        students = savedInstanceState.getParcelableArrayList("Students");
        index = savedInstanceState.getInt("Index");
        layoutType = (LayoutType) savedInstanceState.getSerializable("LayoutType");
        checkedItemsPositions = (SparseBooleanArray) savedInstanceState.getParcelable("CheckedItemPositions");
        itemCount = savedInstanceState.getInt("ItemCount");
        checked = savedInstanceState.getBooleanArray("Checked");

        if (layoutType.equals(LayoutType.SIMPLE_LAYOUT)) {
            setSimpleListView();
        }

        if (layoutType.equals(LayoutType.MULTIPLE_CHOICE_LAYOUT)) {
            setMultipleChoiceView();
            checkElements();
        }

    }

    /**
     * This method is used only to check elements in the listView when the screen is rotated
     * It's not working
     */
    public void checkElements() {
        //   fillArray();
        List<CheckBox> checkBoxes = adapter.getCheckBoxes();
        System.out.println("item count   check   " + itemCount);
        System.out.println("checkedItemsPositions" + checkedItemsPositions.toString());
        System.out.println("boolena " + Arrays.toString(checked));
        for (int i = itemCount - 1; i >= 0; i--) {
            if (checkedItemsPositions.get(i)) {
                Log.i("checked ", "@@@@ " + i);
                listView.setItemChecked(i, true); // not working
            }
        }
    }


    public void getListOfDebtors(String response) throws JSONException {
        JSONObject job = new JSONObject(response);
        String responseType = job.getString("response");
        if(responseType.equals("success")){
            System.out.println("......................");
            JSONArray debtorsJsonArray = new JSONArray(job.getString("debtors"));
            for (int i = 0; i < debtorsJsonArray.length(); i++){
                JSONObject jsonArrayObj = debtorsJsonArray.getJSONObject(i);

                // TODO fix setting image if the path is invalid (null, cant find file because path is invalid eg. hf3662dff )
                Long id;
                Uri uri=null;
                try{
                    id =Long.parseLong(jsonArrayObj.getString("Id"));
                    uri =Uri.parse(jsonArrayObj.getString("Photo"));
                    System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu "+ uri);
                    if(uri == null || uri.equals(Uri.EMPTY) || uri.equals("")  || uri.equals(null)){
                        System.out.println("null URI OOOOOOO");
                        uri = Uri.parse("android.resource://com.example.lab555/" + R.drawable.empty);
                    }
                }catch(Exception e ){
                    id=0L;
                    uri = Uri.parse("android.resource://com.example.lab555/" + R.drawable.empty);

                }

                System.out.println("uri ................. "+ uri);
                Student student = new Student(
                        id,
                        jsonArrayObj.getString("Name"),
                        jsonArrayObj.getString("Phone"),
                        uri
                );
                addStudent(student);
            }
            //adapter.notifyDataSetChanged();
        }
        for(Student debtor : students){
            System.out.println(debtor.toString());
        }
    }

    public void getListOfDebtorsTask(String token){
        LoadJsonTask task = new LoadJsonTask(new MyJsonResponseListener() {
            @Override
            public void onJsonResponseChange(String responseWithDebtorsList) {
                System.out.println("^^^^^^^^^^^^^^^^^^^^"  +responseWithDebtorsList);
                try {
                    getListOfDebtors(responseWithDebtorsList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute("http://apps.ii.uph.edu.pl:88/MSK/MSK/GetDebtors"+"?token="+token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        mDbHelper = new DatabaseOpenHelper(this);
        students = mDbHelper.readDBdata();


        removeButton = findViewById(R.id.removeButton);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelButton);

        listView = findViewById(R.id.listView);

        setSimpleListView();

        token = getIntent().getStringExtra("token");
        System.out.println("token from INTENT " + token);
        getListOfDebtorsTask(token);



    }

}
