package com.example.lab555;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class AddStudentActivity extends AppCompatActivity {

    TextView nameEditText;
    TextView phoneEditText;
    ImageView imageView;
    private static final int PICK_CONTACT_REQUEST = 1;
    Uri photoUri;

    public void importContactsOnClick(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    public Uri getImageUri(long contactId){
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        System.out.println("PHOTO URI @@@@@@@@@@@@@@@@@@@@@@ "+ photoUri );
        return photoUri;

    }
    public Bitmap openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        System.out.println("PHOTO URI @@@@@@@@@@@@@@@@@@@@@@ "+ photoUri );
        Cursor cursor = getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contctDataVar = data.getData();

                    Cursor cursor = getContentResolver().query(contctDataVar, null,
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

                            Bitmap bitmap = openPhoto(Long.parseLong(contactId));
                            if (bitmap == null) {
                                imageView.setImageResource(R.drawable.empty);
                            } else {
                                imageView.setImageBitmap(openPhoto(Long.parseLong(contactId)));
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
        returnIntent.putExtra("photoUri", photoUri);

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

       // imageView.setImageResource(R.drawable.empty);
        imageView.setImageURI(Uri.parse("content://com.android.contacts/contacts/2/photo"));
    }

}
