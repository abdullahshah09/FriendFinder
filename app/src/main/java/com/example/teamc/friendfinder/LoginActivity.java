package com.example.teamc.friendfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public String TAG = "TAG";

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button regBtn;
    private Button mLogInBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        mLogInBtn = (Button) findViewById(R.id.email_sign_in_button);
        regBtn = (Button) findViewById(R.id.createAnAccount);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    //if user is already logged in, will keep logged in till user logs out
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        mLogInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

    }

    public void goToRegForm(View v) {
        Intent i = new Intent(LoginActivity.this, RegisterAccount.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();

                    } else if (user.isEmailVerified() == false) {

                        Toast.makeText(LoginActivity.this, "Email not verified, cannot sign in", Toast.LENGTH_SHORT).show();
                    }
                    else if (user.isEmailVerified() == true) {
                        Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent goToProfile = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(goToProfile);
                        finish();

                    }
                }
            });
        }
    }

}