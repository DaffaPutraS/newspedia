package com.example.newspedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newspedia.Fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassActivity extends AppCompatActivity {

    private EditText emailChangeEditText, passChangeEditText, newPassEditText;

    private Button buttonChangePassword, cancelBtnPassword;

    private FirebaseAuth auth;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        emailChangeEditText = findViewById(R.id.userInputEmailChange);
        passChangeEditText = findViewById(R.id.currentPassTxt);
        newPassEditText = findViewById(R.id.newPassTxt);
        buttonChangePassword = findViewById(R.id.buttonChangePass);
        cancelBtnPassword = findViewById(R.id.cancelBtnChange);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Panggil metode untuk mengisi field email dengan email pengguna yang sedang login
        populateEmailField();

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        cancelBtnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }

    private void populateEmailField() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            emailChangeEditText.setText(email);
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password").setMessage("Are you sure you want to reset your password?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetPassword();
            }
        }).setNegativeButton("No", null).show();
    }

    private void resetPassword() {
        String email = emailChangeEditText.getText().toString().trim();
        String newPassword = newPassEditText.getText().toString().trim();
        String currentPassword = passChangeEditText.getText().toString().trim();
        Log.d("ResetPassword", "Email: " + email);
        Log.d("ResetPassword", "Current Password: " + currentPassword);
        Log.d("ResetPassword", "New Password: " + newPassword);

        if (email.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your email and new password", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            if (!currentPassword.isEmpty()) {
                if (!currentPassword.equals(newPassword)) {
                    // Lakukan tindakan penggantian password
                    AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

                    // Reautentikasi pengguna dengan credential saat ini sebelum mengubah password
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Lakukan penggantian password di Firebase Authentication
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Lakukan penggantian password di Firebase Realtime Database
                                            Intent intent = new Intent(ChangePassActivity.this, ProfileFragment.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ChangePassActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.d("ResetPassword", "Authentication failed: " + task.getException().getMessage());
                                Toast.makeText(ChangePassActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Current password and new password cannot be the same", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}