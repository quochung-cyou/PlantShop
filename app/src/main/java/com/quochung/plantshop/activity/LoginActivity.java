package com.quochung.plantshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.quochung.plantshop.R;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    TextView gotoregister, forgotpassword;
    EditText mEmail, mPassword;
    CardView mLogin;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        loginfunction();
    }


    protected  void initUI() {
        gotoregister = findViewById(R.id.gotosignup);
        mEmail = findViewById(R.id.emaillogin);
        mPassword = findViewById(R.id.passwordlogin);
        mLogin = findViewById(R.id.loginfunction);
        forgotpassword = findViewById(R.id.forgotpassword);
        fAuth = FirebaseAuth.getInstance();
    }





    protected void loginfunction() {

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });




        gotoregister.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
        //Button register click
        mLogin.setOnClickListener(view -> {
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
            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toasty.error(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();


                }
            });
        });

    }
}