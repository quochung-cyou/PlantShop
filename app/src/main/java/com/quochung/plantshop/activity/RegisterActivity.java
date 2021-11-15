package com.quochung.plantshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.quochung.plantshop.R;
import com.quochung.plantshop.activity.LoginActivity;
import com.quochung.plantshop.activity.MainActivity;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    CardView mRegister;
    TextView gotologin;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        registerFunction();
    }


    private void initUI() {
        mEmail = findViewById(R.id.registeremail);
        mPassword = findViewById(R.id.registerpassword);
        mRegister = findViewById(R.id.registerevent);
        fAuth = FirebaseAuth.getInstance();
        gotologin = findViewById(R.id.gotologin);

    }

    private void registerFunction() {
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        gotologin.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });



        //Button register click
        mRegister.setOnClickListener(view -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();



            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }


            if (password.length() < 7) {
                mPassword.setError("Password must longer than 6");
                return;
            }

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toasty.error(this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            });
        });
    }
}