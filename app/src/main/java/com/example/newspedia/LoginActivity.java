package com.example.newspedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTxt,passTxt;
    private TextView signUpTxt;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        interaction();
    }

    private void interaction() {
        signUpTxt.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        loginBtn.setOnClickListener(view -> {
            if (emailTxt.getText().length()>0 && passTxt.getText().length()>0){
                if (emailTxt.getText().toString().equals("admin")&& passTxt.getText().toString().equals("admin")){
                    startActivity(new Intent(this, MainActivity.class));
                    Toast.makeText(this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Email dan Password Salah", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Isi yang bener bang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        signUpTxt = findViewById(R.id.goSignUp);
        emailTxt  = findViewById(R.id.emailRegisterInputText);
        passTxt = findViewById(R.id.passRegisterInputText);
        loginBtn = findViewById(R.id.loginBtn);
    }
}