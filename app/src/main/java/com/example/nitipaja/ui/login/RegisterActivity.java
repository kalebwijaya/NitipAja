package com.example.nitipaja.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitipaja.MainActivity;
import com.example.nitipaja.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserName, mUserEmail, mUserPhone, mUserPassword, mUserRePassword;
    private Button mRegisterButton;
    private TextView mToLogin;
    private FirebaseAuth fAuth;
    private DatabaseReference fDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName = findViewById(R.id.et_reigister_fullname);
        mUserEmail = findViewById(R.id.et_reigister_email);
        mUserPhone = findViewById(R.id.et_reigister_phone);
        mUserPassword = findViewById(R.id.et_regiester_password);
        mUserRePassword = findViewById(R.id.et_regiester_repassword);
        mRegisterButton = findViewById(R.id.btn_register_register);
        mToLogin = findViewById(R.id.tv_to_login);

        fAuth = FirebaseAuth.getInstance();
        fDatabaseRef = FirebaseDatabase.getInstance().getReference("user");

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mUserEmail.getText().toString().trim();
                final String password = mUserPassword.getText().toString().trim();
                final String phone = mUserPhone.getText().toString().trim();
                final String fullName = mUserName.getText().toString().trim();

                if(validateData()){
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UserModel userModel = new UserModel(
                                        fAuth.getUid(),
                                        fullName,
                                        email,
                                        phone
                                        );
                                fDatabaseRef.child(fAuth.getUid()).setValue(userModel);

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        mToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validateData(){
        String email = mUserEmail.getText().toString().trim();
        String password = mUserPassword.getText().toString().trim();
        String rePassword = mUserRePassword.getText().toString().trim();
        String phone = mUserPhone.getText().toString().trim();
        String fullName = mUserName.getText().toString().trim();

        if(email.isEmpty()){
            mUserEmail.setError("E-mail Tidak Boleh Kosong");
            return false;
        }

        if(password.isEmpty()){
            mUserPassword.setError("Sandi Tidak Boleh Kosong");
            return false;
        }

        if(password.length() < 8){
            mUserPassword.setError("Sandi Minimal 8 Karakter");
            return false;
        }

        if(!rePassword.equals(password)){
            mUserRePassword.setError("Sandi Tidak Cocok");
            return false;
        }

        if(phone.isEmpty()){
            mUserPhone.setError("Nomor Tidak Boleh Kosong");
            return false;
        }

        if(fullName.isEmpty()){
            mUserName.setError("Nama Tidak Boleh Kosong");
            return false;
        }

        return true;
    }


}