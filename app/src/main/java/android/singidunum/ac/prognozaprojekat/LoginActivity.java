package android.singidunum.ac.prognozaprojekat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.singidunum.ac.prognozaprojekat.prefs.SharedPrefs;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private final static String SHARED_PREFFERENCES_PASSWORD = "password";
    private final static String SHARED_PREFFERENCES_EMAIL = "email";

    private EditText inputPassword, inputEmail;
    private Button buttonRememberme, buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
    }

    private void initComponents(){
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRememberme = (Button) findViewById(R.id.buttonRememberme);

        buttonRememberme.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    public void openWeather(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonLogin:
                login();
                break;
            case R.id.buttonRememberme:
                ucitajPodatke();
                break;
        }
    }

    private void login(){
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();


        if(email.trim().length() > 0 && password.trim().length() > 0) {

            if (    SharedPrefs.getInstance(this).getPrefs().getString(SHARED_PREFFERENCES_EMAIL, null).equals(email) &&
                    SharedPrefs.getInstance(this).getPrefs().getString(SHARED_PREFFERENCES_PASSWORD, null).equals(password)
            ){
                openWeather();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Password or Email incorrect.",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter Email and Password",
                    Toast.LENGTH_LONG).show();
        }

    }


    private void ucitajPodatke(){
        SharedPreferences prefs = SharedPrefs.getInstance(this).getPrefs();

        String uMail = prefs.getString(SHARED_PREFFERENCES_EMAIL, null);
        String uPassword = prefs.getString(SHARED_PREFFERENCES_PASSWORD, null);

        inputEmail.setText(uMail);
        inputPassword.setText(uPassword);
    }
}