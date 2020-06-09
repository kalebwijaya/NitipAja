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

public class EditProfileActivity extends AppCompatActivity {

    private EditText mUserNewName, mUserNewEmail, mUserNewPhone;
    private Button mEditProfile;

    private FirebaseUser fUser;
    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private UserModel currentUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mUserNewName = findViewById(R.id.et_edit_profile_fullname);
        mUserNewEmail = findViewById(R.id.et_edit_profile_email);
        mUserNewPhone = findViewById(R.id.et_edit_profile_phone);
        mEditProfile = findViewById(R.id.btn_edit_profile_edit);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    UserModel userModel = postSnapshot.getValue(UserModel.class);
                    if(userModel.getUserID().equals(fAuth.getUid())){
                        currentUserModel = userModel;
                        mUserNewEmail.setText(userModel.getUserEmail());
                        mUserNewName.setText(userModel.getUserName());
                        mUserNewPhone.setText(userModel.getUserPhonenumber());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUserNewEmail.getText().toString().trim();
                String phone = mUserNewPhone.getText().toString().trim();
                String fullName = mUserNewName.getText().toString().trim();
                if(validateData()){
                    currentUserModel.setUserEmail(email);
                    currentUserModel.setUserPhonenumber(phone);
                    currentUserModel.setUserName(fullName);

                    fUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                databaseReference.child(currentUserModel.getUserID()).setValue(currentUserModel);
                                Toast.makeText(EditProfileActivity.this,"Sukses Ubah Profil", Toast.LENGTH_SHORT).show();
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
        String email = mUserNewEmail.getText().toString().trim();
        String phone = mUserNewPhone.getText().toString().trim();
        String fullName = mUserNewName.getText().toString().trim();

        if(email.isEmpty()){
            mUserNewEmail.setError("E-mail Tidak Boleh Kosong");
            return false;
        }

        if(phone.isEmpty()){
            mUserNewPhone.setError("Nomor Tidak Boleh Kosong");
            return false;
        }

        if(fullName.isEmpty()){
            mUserNewName.setError("Nama Tidak Boleh Kosong");
            return false;
        }

        return true;
    }

}