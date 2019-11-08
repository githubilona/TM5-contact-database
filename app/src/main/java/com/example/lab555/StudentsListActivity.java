package com.example.lab555;

import android.app.Activity;
import android.content.Intent;
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
    boolean [] checked;

    public void addStudent(Student student) {
        students.add(student);
        index++;
        adapter.notifyDataSetChanged();
    }

    private void fillArray() {
        checkedItemsPositions = listView.getCheckedItemPositions();
        itemCount = listView.getCount();
        checked=adapter.getChecked();

        for (int i = 0; i < itemCount; i++) {
            checkedItemsPositions.put(i, checked[i]);
        }
    }

    public void deleteOnClick(View view) {
        fillArray();

        for (int i = itemCount - 1; i >= 0; i--) {
            if (checkedItemsPositions.get(i)) {
                checked[i] = false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // data.getOb
                CharSequence resultName = data.getCharSequenceExtra("resultName");
                CharSequence resultPhone = data.getCharSequenceExtra("resultPhone");
                // Obsługa rezultatów które otrzymaliśmy z wywołanej aktywności
                System.out.println("RESULT NAME)))))))))))))))))))))" + resultName);
                System.out.println("RESULT PHONE)))))))))))))))))))))" + resultPhone);

//                addStudent(resultName + " " + resultPhone);
                addStudent(new Student(resultName + "", resultPhone + ""));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // W przpyapku otrzymania błędnych rezultatów
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
        //savedInstanceState.putParcelableArrayList("Students", (ArrayList<? extends Parcelable>) new ArrayList<Student>(students));
        savedInstanceState.putInt("Index", index);
        savedInstanceState.putSerializable("LayoutType", layoutType);


        checkedItemsPositions = listView.getCheckedItemPositions();
        itemCount = listView.getCount();

        if (checkedItemsPositions != null) {

            fillArray();
            savedInstanceState.putParcelable("CheckedItemPositions", new SparseBooleanArrayParcelable(checkedItemsPositions));
            savedInstanceState.putInt("ItemCount", itemCount);
            savedInstanceState.putBooleanArray("Checked", checked);

            System.out.println("-5-6-6--7--7-8--88-9-9-----" + Arrays.toString(checked) );
            System.out.println("--4-66-456-57---7567-567--57-567-----" +checkedItemsPositions.toString() );
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        //myStrings = savedInstanceState.getStringArrayList("MyStrings");

        students = savedInstanceState.getParcelableArrayList("Students");
        index = savedInstanceState.getInt("Index");
        layoutType = (LayoutType) savedInstanceState.getSerializable("LayoutType");
        checkedItemsPositions = (SparseBooleanArray) savedInstanceState.getParcelable("CheckedItemPositions");
        itemCount = savedInstanceState.getInt("ItemCount");
        checked = savedInstanceState.getBooleanArray("Checked");
       // fillArray();

        System.out.println("-----------------" + Arrays.toString(checked) );
        System.out.println("-----------------" +checkedItemsPositions.toString() );

        System.out.println("items count " + itemCount);

        if (layoutType.equals(LayoutType.SIMPLE_LAYOUT)) {
            setSimpleListView();
        }

        if (layoutType.equals(LayoutType.MULTIPLE_CHOICE_LAYOUT)) {

            System.out.println("2-----------------" + Arrays.toString(checked) );
            System.out.println("2-----------------" +checkedItemsPositions.toString() );
            //fillArray();
            setMultipleChoiceView();

            checkElements();

            System.out.println("-----------------" + Arrays.toString(checked) );
            System.out.println("-----------------" +checkedItemsPositions.toString() );
        }

    }

    public void checkElements() {
     //   fillArray();
        List<CheckBox> checkBoxes=adapter.getCheckBoxes();
        //System.out.println("-r-394uto83yghiu453t8-----------------" + checkBoxes.toString());
         System.out.println("item count   check   "+ itemCount);
        System.out.println("checkedItemsPositions" + checkedItemsPositions.toString());
        System.out.println("boolena "+ Arrays.toString(checked));
        for (int i = itemCount - 1; i >= 0; i--) {
            if (checkedItemsPositions.get(i)) {
                Log.i("checked ", "@@@@ " + i);
                listView.setItemChecked(i,true);

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        removeButton = findViewById(R.id.removeButton);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelButton);

        listView = findViewById(R.id.listView);
        students = new ArrayList<>();
        setSimpleListView();
       // checked= adapter.getChecked();


        students.add(new Student("Contact 1 ", "343-545-354"));
        students.add(new Student("Contact 2 ", "676-567-863"));
        students.add(new Student("Contact 3 ", "965-667-863"));


    }

}
