package com.inderproduction.theinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.inderproduction.theinderapp.Utilities.DisplayUtils;
import com.inderproduction.theinderapp.Utilities.Validations;

public class Signup extends AppCompatActivity {
    private EditText signupEmail,password,confirmPassword;
    private Button signupButton;
    private TextView alreadyUser;
    private ProgressBar signUpProgress;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        signupEmail=findViewById(R.id.signup_email);
        password=findViewById(R.id.signup_password);
        confirmPassword=findViewById(R.id.confirm_password);
        alreadyUser=findViewById(R.id.signup_already_user);
        signupButton=findViewById(R.id.signup_button);
        signUpProgress = findViewById(R.id.signup_progress);

        DisplayUtils.disableFields(signupButton);

        signupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Validations.isEmailValid(signupEmail);
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!confirmPassword.getText().toString().trim().equals("")){
                    DisplayUtils.disableFields(password);
                }

                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                   DisplayUtils.enableFields(signupButton);
                }else{
                    DisplayUtils.disableFields(signupButton);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations.isEmailValid(signupEmail)) {
                    signUpProgress.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(signupEmail.getText().toString().toLowerCase(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    signUpProgress.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                            if (firebaseAuth.getCurrentUser() != null) { Intent toMain = new Intent(Signup.this,ShoppingActivity.class);
                                            startActivity(toMain);
                                            finish();
                                        }
                                    } else {
                                        DisplayUtils.showToast(Signup.this, "Invalid Creds", Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                }
            }
        });
    }
}
