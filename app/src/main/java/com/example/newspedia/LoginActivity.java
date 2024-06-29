package com.example.newspedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTxt,passTxt;
    private TextView signUpTxt;
    private Button loginBtn;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth mAuth;
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
                login(emailTxt.getText().toString(),passTxt.getText().toString());
            }else {
                Toast.makeText(this, "Isi yang bener bang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String email, String password) {
        DatabaseReference userRef = firebaseDatabase.getReference().child("users");

        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            reload();
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "Gagal login", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "Akun tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    private void initView(){
        signUpTxt = findViewById(R.id.goSignUp);
        emailTxt  = findViewById(R.id.emailRegisterInputText);
        passTxt = findViewById(R.id.passRegisterInputText);
        loginBtn = findViewById(R.id.loginBtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }
}