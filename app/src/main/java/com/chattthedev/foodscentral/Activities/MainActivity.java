package com.chattthedev.foodscentral.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chattthedev.foodscentral.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView signup;
    Button login;
    EditText e1, e2;
    Button registernew;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistner;
    long back_pressed;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        circleImageView = findViewById(R.id.circularimageview);
        signup = findViewById(R.id.signuppage);
        circleImageView.bringToFront();
        login = findViewById(R.id.login);
        e1 = findViewById(R.id.cusemail);
        e2 = findViewById(R.id.cuspass);
        mAuthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    Intent i = new Intent(getApplicationContext(), Main_Dashboard.class);
                    startActivity(i);
                }

            }
        };

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString().trim();
                String password = e2.getText().toString().trim();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 5) {

                    Toast.makeText(MainActivity.this, "Password Should Be Minimum 6 Digits", Toast.LENGTH_LONG).show();
                    return;

                }
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Logging You In!!!");
                progressDialog.setMessage("Processing.....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successfull!!", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(MainActivity.this, Main_Dashboard.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                e1.setText("");
                e2.setText("");

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();


    }
}