package com.quochung.plantshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.quochung.plantshop.R;

import es.dmoral.toasty.Toasty;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mEmail;
    CardView mForgotPass, mCancel;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUI();
        forgotPasswordfunction();


    }

    private void initUI() {
        mEmail = findViewById(R.id.forgotpasswordemail);
        mForgotPass = findViewById(R.id.forgotpasswordfunction);
        fAuth = FirebaseAuth.getInstance();
        mCancel = findViewById(R.id.forgotpasswordcancel);

    }

    private void forgotPasswordfunction() {
        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();



                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }

                fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toasty.success(ForgotPasswordActivity.this, "Sent email!", Toast.LENGTH_SHORT, true).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                });


            }
        });


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }


}