package com.example.lab555;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText loginEditText;
    EditText passwordEditText;
    CheckBox rememberMeCheckBox;
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;


    private void rememberMe(String login, String password){
        preferencesEditor.putString("login", login);
        preferencesEditor.putString("password", password);

        boolean saved = preferencesEditor.commit(); //jak nas nie obchodzi czy sie zapisze to można apply ()
        if(!saved){
            Toast.makeText(this, getText(R.string.login_data_save_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void loginOnClick(View view) {
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(login.isEmpty() || password.isEmpty()){
            // res.values.strings.strings.xml
            loginEditText.setError(getString(R.string.login_empty_error));
            passwordEditText.setError(getString(R.string.login_empty_error));
        }

        if(rememberMeCheckBox.isChecked()){
           rememberMe(login,password);
        }
        Intent debtsListIntent = new Intent(this, StudentsListActivity.class);
        startActivity(debtsListIntent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        preferences = this.getPreferences(MODE_PRIVATE);
        // pobranie preferencji dla aktywności MODE_PRIVATE oznacza że obiekt będzie
        // dostępny jedynie w ramach aplikacji(lub inne aplikacje dzielące ten sam id użytkownika)
        preferencesEditor = preferences.edit(); //pobranie edytora umożliwiającego

        String login = preferences.getString("login","");
        String password= preferences.getString("password","");
        loginEditText.setText(login);
        passwordEditText.setText(password);


    }
}
