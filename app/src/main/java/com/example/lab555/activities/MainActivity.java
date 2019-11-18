package com.example.lab555.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab555.R;
import com.example.lab555.model.Student;
import com.example.lab555.tasks.LoadJsonTask;
import com.example.lab555.tasks.LoadLoginTask;
import com.example.lab555.tasks.MyJsonResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText loginEditText;
    EditText passwordEditText;
    CheckBox rememberMeCheckBox;
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;




    public String getResponseToken(JSONObject response) throws JSONException {
        String responseType = response.getString("response");
        String token="";
        if(responseType.equals("success")){
            token = response.getString("token");
            System.out.println("token + "+ token);
        }
        return token;
    }
    public String loginAuthentication(String login, String password){
        LoadLoginTask doPost = new LoadLoginTask();
        String token="";
        try {
            String response = doPost.execute("http://apps.ii.uph.edu.pl:88/MSK/MSK/Authenticate", "login=" +
                    URLEncoder.encode(login, "UTF-8") , "password=" +
                    URLEncoder.encode(password, "UTF-8")).get();
            System.out.println("login on click " + "-------------  RESPONSE "+ response);

            JSONObject jsonObject = new JSONObject(response);
            token = getResponseToken(jsonObject);
            System.out.println("token token token token token token  + " + token);

            //getListOfDebtorsTask(token);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;

    }
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

        String token =loginAuthentication(login, password);
        Intent debtsListIntent = new Intent(this, StudentsListActivity.class);
        if(!token.equals("")){
            debtsListIntent.putExtra("token", token);
            startActivity(debtsListIntent);
        }else{
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }


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
