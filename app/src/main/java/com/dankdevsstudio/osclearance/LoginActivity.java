package com.dankdevsstudio.osclearance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String APP_SECRET = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText appPassword = findViewById(R.id.passwordEditText);
                if (APP_SECRET.equals(appPassword.getText().toString())){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please ask OSC for the password", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, ConfirmationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
