package com.inderproduction.theinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.inderproduction.theinderapp.Utilities.Validations;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button loginbutton;
    private TextView newuser, forgetpassword, skipLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 1;
    private boolean isStartingActivity = true;
    LinearLayout googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (auth.getCurrentUser() != null) {
            Intent loginIntent = new Intent(LoginActivity.this, ShoppingActivity.class);
            startActivity(loginIntent);
            finish();
        }

        username = findViewById(R.id.login_username);
        googleLogin = findViewById(R.id.googleLogin);
        password = findViewById(R.id.login_password);
        loginbutton = findViewById(R.id.login_button);
        newuser = findViewById(R.id.login_newuser);
        skipLogin = findViewById(R.id.login_skip);
        loginProgress = findViewById(R.id.login_progress);

        if (getIntent().getStringExtra("sender") != null) {
            isStartingActivity = false;
            skipLogin.setVisibility(View.GONE);
        }
        ;


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validations.isUsernameValid(username);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations.isEmailValid(username)) {
                    loginProgress.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    loginProgress.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        if (isStartingActivity) {
                                            Intent loginIntent = new Intent(LoginActivity.this, ShoppingActivity.class);
                                            startActivity(loginIntent);

                                        }
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Incorrect Login", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent skipLoginIntent = new Intent(LoginActivity.this, ShoppingActivity.class);
                startActivity(skipLoginIntent);
                finish();
            }
        });

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newUserIntent = new Intent(LoginActivity.this, Signup.class);
                startActivity(newUserIntent);
                finish();
            }

        });
    }

    private void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("Google", gso.toString());
    }

    private void googleSignOut() {
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        try {

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                // Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    if (isStartingActivity) {
                                        googleSignOut();
                                        Intent loginIntent = new Intent(LoginActivity.this, ShoppingActivity.class);
                                        startActivity(loginIntent);

                                    }
                                    finish();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                                //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Authentication Failed",
                                        Toast.LENGTH_SHORT).show();
                            }


                            // ...
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
