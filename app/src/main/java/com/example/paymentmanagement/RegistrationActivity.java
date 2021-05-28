package com.example.paymentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity{
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    TextInputEditText userNameTIL,emailTIL, passwordTIL, passwordAgainTIL;
    ImageView bankCardIcon;
    TextView welcomeTV, signUpToContinueTV;
    RadioGroup accountTypeRG;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        bankCardIcon = findViewById(R.id.bankCardIcon);
        welcomeTV = findViewById(R.id.welcomeTV);
        signUpToContinueTV = findViewById(R.id.signInToContinueTV);
        userNameTIL = findViewById(R.id.userNameInput);
        emailTIL = findViewById(R.id.emailInput);
        passwordTIL = findViewById(R.id.passwordInput);
        passwordAgainTIL = findViewById(R.id.passwordAgainInput);
        accountTypeRG = findViewById(R.id.accountTypeRadioGroup);
        accountTypeRG.check(R.id.personal);

        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        emailTIL.setText(email);
        passwordTIL.setText(password);
        passwordAgainTIL.setText(password);
    }


    public void register(View view){
        String userName = (String)userNameTIL.getText().toString();
        String email = (String)emailTIL.getText().toString();
        String password = (String)passwordTIL.getText().toString();
        String passwordAgain = (String)passwordAgainTIL.getText().toString();

        if(!password.equals(passwordAgain)){
            Log.e(LOG_TAG,"Passwords don't match!");
            return;
        }

        int accountTypeId = accountTypeRG.getCheckedRadioButtonId();
        View radioButton = accountTypeRG.findViewById(accountTypeId);
        int id = accountTypeRG.indexOfChild(radioButton);
        String accountType = ((RadioButton) accountTypeRG.getChildAt(id)).getText().toString();

        Log.i(LOG_TAG,"Signed Up: " + userName + ", email: "+email);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    currentUserId = mAuth.getUid();
                    Log.d(LOG_TAG, "User created successfully");
                    startManaging();
                } else {
                    Log.d(LOG_TAG, "User was't created successfully:", task.getException());
                    Toast.makeText(RegistrationActivity.this, "User was't created successfully:", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void cancel(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startManaging(/* registered used class */) {
        Intent intent = new Intent(this, PaymentManagingActivity.class);
        startActivity(intent);
        Log.d(LOG_TAG, "Start Managing");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

}