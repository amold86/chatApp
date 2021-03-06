package com.example.hp.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by HP on 26-03-2018.
 */

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {
    private EditText loginEmail;
    private EditText loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView txtRegisterHere;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail =(EditText)findViewById(R.id.loginEmail);
        loginPass=(EditText)findViewById(R.id.loginPass);
        txtRegisterHere=findViewById(R.id.txtRegisterHere);



        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        txtRegisterHere.setOnClickListener(this);

    }
    public void loginButtonClicked(View view){
        String email =loginEmail.getText().toString().trim();
        String password =loginPass.getText().toString().trim();

        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        CheckUserExists();
                    }
                }

            });
        }

    }
    public void CheckUserExists(){
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)){
                    Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.txtRegisterHere:
            Intent gotoRegisterIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(gotoRegisterIntent);
            break;
        }
    }
}
