package com.example.lab555.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab555.R;

import java.io.IOException;

public class AddStudentActivity extends AppCompatActivity {

    TextView nameEditText;
    TextView phoneEditText;
    ImageView imageView;
    private static final int PICK_CONTACT_REQUEST = 1;
    String photoUri;


    public void importContactsOnClick(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactDataVar = data.getData();

                    Cursor cursor = getContentResolver().query(contactDataVar, null,
                            null, null, null);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                            nameEditText.setText(contactName);

                            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                // Query phone here. Covered next
                                String ContctMobVar = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.i("Number", ContctMobVar);
                                phoneEditText.setText(ContctMobVar);
                            }

                            photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            if (photoUri == null) {
                                imageView.setImageResource(R.drawable.empty);
                            } else {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(photoUri));
                                    imageView.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
                break;
        }

    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == PICK_CONTACT_REQUEST) {
//           if (resultCode == RESULT_OK) {
////                // Pobieramy URI przekazany z książki adresowej do wybranego kontaktu
////                Uri contactUri = data.getData();
////                // Potrzebujemy tylko imienia
////                String[] projection =
////                        {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
////                String[] phone =
////                        {ContactsContract.CommonDataKinds.Phone.NUMBER};
////                // Powinniśmy to robić w oddzielnym wątku bo operacje z użyciem kursora
////                // mogą być czasochłonne.Można wykorzystać klasę CursorLoader.
////                Cursor cursor = getContentResolver()
////                        .query(contactUri, projection, null, null, null);
////                //pobranie pierwszego wiersza
////                cursor.moveToFirst();
////                // Pobranie kolumny o odpowiednim indeksie
////                int column =
////                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME);
////                String displayName = cursor.getString(column);
////                TextView nameEditText = (TextView) findViewById(R.id.nameEditText);
////                TextView phoneEditText = findViewById(R.id.phoneEditText);
////
////                nameEditText.setText(displayName);
////                System.out.println("/..........................." + phone);
////                for(String p :phone){
////                    System.out.println("........." + p);
////                }
////                phoneEditText.setText(Arrays.toString(phone));
////
//
//           }
//        }
//    }

    public void onFinishAddClick(View view) {
        Intent returnIntent = getIntent();
        CharSequence name = nameEditText.getText();
        CharSequence phone = phoneEditText.getText();

        returnIntent.putExtra("resultName", name);
        returnIntent.putExtra("resultPhone", phone);


       if(photoUri == null){
           Uri emptyPhotoUri = Uri.parse("android.resource://com.example.lab555/" + R.drawable.empty);
           returnIntent.putExtra("photoUri",emptyPhotoUri );
       }else{
           returnIntent.putExtra("photoUri", Uri.parse(photoUri));
       }

        if (name.toString().equals("") || phone.toString().equals("")) {
            setResult(Activity.RESULT_CANCELED, returnIntent);
        } else {
            setResult(Activity.RESULT_OK, returnIntent);
        }

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        imageView = findViewById(R.id.imageView);

        imageView.setImageResource(R.drawable.empty);
        //  imageView.setImageURI(Uri.parse("content://com.android.contacts/display_photo/4"));

    }

}
