package com.example.foody_express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Objects;

public class login extends AppCompatActivity {
    ImageView googlebtn;
    ImageView fbbtn;

    TextView forgottext;
    CallbackManager callbackManager;

    EditText fEmail , fPassword;
    Button fButton;
    ProgressBar fprogress;
    FirebaseAuth fAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googlebtn = findViewById(R.id.img);
        fbbtn = findViewById(R.id.img2);
        forgottext =findViewById(R.id.forgot1);


        fEmail = findViewById(R.id.enteremail);
        fPassword =findViewById(R.id.enterpass);
        fprogress = findViewById(R.id.progressbar2);
        fAuth=FirebaseAuth.getInstance();
        fButton = findViewById(R.id.loginbutton1);

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = fEmail.getText().toString().trim();
                String password = fPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    fEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    fEmail.setError("password is Required");
                    return;
                }

                if(password.length()<8){
                    fPassword.setError("password must be >= 8 characters");
                    return;
                }
                fprogress.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), dashboard.class));

                        }else {
                            Toast.makeText(login.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            fprogress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        forgottext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                LayoutInflater inflater = getLayoutInflater();
                View view1= inflater.inflate(R.layout.forgot,null);
                final EditText userEmail = view1.findViewById(R.id.editbox);
                builder.setView(view1);
                final AlertDialog dialog = builder.create();

                view1.findViewById(R.id.btnreset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        compareEmail(userEmail);
                        dialog.dismiss();
                    }
                });
                view1.findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });



        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent intent = new Intent(login.this , dashboard.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //google login
            }
        });
        fbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("public_profile"));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void signin1(View view) {
        Intent intent = new Intent(login.this,registration.class);
        startActivity(intent);
    }





    private void compareEmail(EditText userEmail) {
        if(userEmail.getText().toString().isEmpty()){
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()){
            return;
        }
        fAuth= FirebaseAuth.getInstance();
        fAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(login.this,"Check Your Email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}