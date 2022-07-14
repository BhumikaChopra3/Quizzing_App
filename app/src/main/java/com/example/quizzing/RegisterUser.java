package com.example.quizzing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity  {

    private ImageView banner;
    private TextView registerUser;
    private EditText fullName,Age,email,password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (ImageView) findViewById(R.id.registerimage);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.registerimage:
                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                        break;
                    case R.id.register:
                        register();
                        break;
                }
            }
        });

        registerUser = (Button)findViewById(R.id.register);
        fullName = (EditText) findViewById(R.id.fullName);
        Age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

    }


    private void register() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String name = fullName.getText().toString().trim();
        String age = Age.getText().toString().trim();

        if(name.isEmpty()){
            fullName.setError("Name is required");
            fullName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            Age.setError("Age is required");
            Age.requestFocus();
            return;
        }
        if(mail.isEmpty()){
            email.setError("email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("please provide valid email!");
            email.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(pass.length() < 6){
            email.setError("Min password length should be 6 characters!");
            email.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            user user = new user(name,age,mail);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been registered Successfully", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(RegisterUser.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}