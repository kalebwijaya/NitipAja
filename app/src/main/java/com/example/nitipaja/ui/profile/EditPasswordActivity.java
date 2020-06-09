package com.example.nitipaja.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nitipaja.MainActivity;
import com.example.nitipaja.R;
import com.example.nitipaja.ui.login.RegisterActivity;
import com.example.nitipaja.ui.login.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPasswordActivity extends AppCompatActivity {

    private EditText mOldPassword, mNewPassword, mNewRepassword;
    private Button mEditPassword;

    private FirebaseUser fUser;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        mOldPassword = findViewById(R.id.et_edit_pass_old_pass);
        mNewPassword = findViewById(R.id.et_edit_pass_new_pass);
        mNewRepassword = findViewById(R.id.et_edit_pass_new_repass);
        mEditPassword = findViewById(R.id.btn_edit_password_edit);

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        mEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = mNewPassword.getText().toString().trim();
                if(validateData()){
                    fUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditPasswordActivity.this,"Sukses Ubah Password", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean validateData(){
        String oldPassword = mOldPassword.getText().toString().trim();
        String password = mNewPassword.getText().toString().trim();
        String rePassword = mNewRepassword.getText().toString().trim();

        if(oldPassword.isEmpty()){
            mOldPassword.setError("Sandi Lama Tidak Boleh Kosong");
            return false;
        }

        if(password.isEmpty()){
            mNewPassword.setError("Sandi Baru Tidak Boleh Kosong");
            return false;
        }

        if(password.length() < 8){
            mNewPassword.setError("Sandi Baru Minimal 8 Karakter");
            return false;
        }

        if(!rePassword.equals(password)){
            mNewRepassword.setError("Sandi Baru Tidak Cocok");
            return false;
        }

        return true;
    }
}