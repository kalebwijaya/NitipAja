package com.example.nitipaja.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nitipaja.R;
import com.example.nitipaja.ui.login.LoginActivity;
import com.example.nitipaja.ui.login.UserModel;
import com.example.nitipaja.ui.transaction.request.TabRequestAdapter;
import com.example.nitipaja.ui.transaction.request.TabRequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView mUsername, mUserEmail, mUserPhone;
    private Button mEditProfile, mEditPassword, mLogout;

    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mUsername = root.findViewById(R.id.tv_profile_username);
        mUserEmail = root.findViewById(R.id.tv_profile_email);
        mUserPhone = root.findViewById(R.id.tv_profile_phone);
        mEditProfile = root.findViewById(R.id.btn_profile_edit_profile);
        mEditPassword = root.findViewById(R.id.btn_profile_edit_password);
        mLogout = root.findViewById(R.id.btn_profile_logout);

        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    UserModel userModel = postSnapshot.getValue(UserModel.class);
                    if(userModel.getUserID().equals(fAuth.getUid())){
                        mUsername.setText(userModel.getUserName());
                        mUserEmail.setText(userModel.getUserEmail());
                        mUserPhone.setText(userModel.getUserPhonenumber());
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
                Intent intent = new Intent(getContext(),EditProfileActivity.class);
                getContext().startActivity(intent);
            }
        });

        mEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),EditPasswordActivity.class);
                getContext().startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return root;
    }
}