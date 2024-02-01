package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailTxt,passTxt,usernameTxt;
    private TextView signInTxt;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        Interaction();
    }

    private void Interaction() {
        signInTxt.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        registerBtn.setOnClickListener(view -> {
            if(usernameTxt.getText().length()>0 && emailTxt.getText().length()>0 && passTxt.getText().length()>0){
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "User dibuat", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Isi yang bener bang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        signInTxt = findViewById(R.id.goSignIn);
        usernameTxt = findViewById(R.id.userInputTxt);
        emailTxt  = findViewById(R.id.emailRegisterInputText);
        passTxt = findViewById(R.id.passRegisterInputText);
        registerBtn = findViewById(R.id.buttonRegisterSubmit);
    }
}