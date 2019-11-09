package com.example.lab555.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab555.R;
import com.example.lab555.pojo.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentsAdapter extends BaseAdapter {

    private Context context;
    int layout;
    List<Student> students;
    boolean[] checked;
    List<CheckBox> checkBoxes;
    private boolean isChecked = false;

    public StudentsAdapter(Context context, int layout, List<Student> students) {
        this.context = context;
        this.layout = layout;
        this.students = students;
        checked = new boolean[students.size()];
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int i) {
        return students.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(layout, null);
        TextView studentName = row.findViewById(R.id.studentNameTextView);
        TextView studentPhone = row.findViewById(R.id.studentPhoneTextView);
        ImageView image = row.findViewById(R.id.imageView);

        Student student = students.get(i);
        studentName.setText(student.getName());
        studentPhone.setText(student.getPhone() + "");
        image.setImageURI(student.getImageUri());


        if(layout==R.layout.student_list_item_multiple_choice){
            checkBoxes = new ArrayList<>();
            CheckBox checkBox = row.findViewById(R.id.checkBox);
        checkBox.setTag(Integer.valueOf(i)); // set the tag so we can identify the correct row in the listener
        checkBox.setChecked(checked[i]); // set the status as we stored it
        checkBox.setOnCheckedChangeListener(mListener); // set the listener
            checkBoxes.add(checkBox);

       }

        return row;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checked[(Integer)buttonView.getTag()] = isChecked; // get the tag so we know the row and store the status
            System.out.println("listiner "+ Arrays.toString(checked));
        }
    };

    public void remove(int position) {
        students.remove(students.get(position));
    }


    public boolean[] getChecked() {
        return checked;
    }

    public List<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }


}
