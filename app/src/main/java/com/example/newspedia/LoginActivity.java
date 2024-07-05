package com.example.newspedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTxt,passTxt;
    private TextView signUpTxt , forgotPasswordTxt;
    private Button loginBtn;

    private FirebaseDatabase firebaseDatabase;
    private TextView loginSuccessMessage;
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
        forgotPasswordTxt.setOnClickListener(view -> {
            startActivity(new Intent (this, ForgotPassActivity.class));
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
                            Toast.makeText(LoginActivity.this, "Berhasil login", Toast.LENGTH_SHORT).show();
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


        private void showBottomSheetSuccess() {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
            View view1 = LayoutInflater.from(LoginActivity.this).inflate(R.layout.bottom_side_login_success, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();

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
        loginSuccessMessage = findViewById(R.id.login_success_message);

        forgotPasswordTxt = findViewById(R.id.btnForgotPassword);
        signUpTxt = findViewById(R.id.goSignUp);
        emailTxt  = findViewById(R.id.emailRegisterInputText);
        passTxt = findViewById(R.id.passRegisterInputText);
        loginBtn = findViewById(R.id.loginBtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }
}