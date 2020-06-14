package com.example.nitipaja.ui.home.orderProduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitipaja.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class OrderProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String childName;
    private String userID;
    private String status = "Menunggu";
    private String selectedCategory;
    private Button btnChooseImage, btnPlaceRequest;
    private EditText etProductName, etProductLocation, etProductQuantity, etProductDescription, etProductPrice;
    private ImageView ivPreviewImage;
    private Spinner categorySpinner;

    private Uri mImageUri;

    private FirebaseAuth fAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        btnChooseImage = findViewById(R.id.btn_home_upload_photo);
        btnPlaceRequest = findViewById(R.id.btn_home_place_request);

        etProductName = findViewById(R.id.et_home_product_name);
        etProductLocation = findViewById(R.id.et_home_product_location);
        etProductQuantity = findViewById(R.id.et_home_product_quantity);
        etProductDescription = findViewById(R.id.et_home_product_description);
        ivPreviewImage = findViewById(R.id.iv_home_image_preview);
        categorySpinner = findViewById(R.id.spinner_category);
        etProductPrice = findViewById(R.id.et_home_product_price);

        storageReference = FirebaseStorage.getInstance().getReference("productImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("transaction");

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnPlaceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){

                }else{
                    placeRequest();
                }
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            ivPreviewImage.setImageURI(mImageUri);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void placeRequest(){
        if(validate() == true && mImageUri != null){
            childName = System.currentTimeMillis()+"."+getFileExtension(mImageUri);
            StorageReference fileReferences = storageReference.child(childName);
            mUploadTask = fileReferences.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(OrderProductActivity.this, "Sukses Pesan Barang", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    OrderProductModel upload = new OrderProductModel(
                                            databaseReference.push().getKey(),
                                            etProductName.getText().toString(),
                                            etProductLocation.getText().toString(),
                                            etProductPrice.getText().toString(),
                                            etProductDescription.getText().toString(),
                                            selectedCategory,
                                            etProductQuantity.getText().toString(),
                                            userID,
                                            uri.toString(),
                                            status, ""
                                    );

                                    databaseReference.child(databaseReference.push().getKey()).setValue(upload);
                                }
                            });
                            OrderProductActivity.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OrderProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }else{
            Toast.makeText(this,"Silahkan Pilih Gambar", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(){
        if(etProductName.getText().toString().trim().isEmpty()){
            etProductName.setError("Nama Produk Tidak Boleh Kosong");
            return false;
        }else if(etProductLocation.getText().toString().trim().isEmpty()){
            etProductLocation.setError("Lokasi Produk Tidak Boleh Kosong");
            return false;
        }else if(etProductPrice.getText().toString().trim().isEmpty()){
            etProductPrice.setError("Harga Produk Tidak Boleh Kosong");
            return false;
        }else if(etProductQuantity.getText().toString().trim().isEmpty()){
            etProductQuantity.setError("Jumlah Produk Tidak Boleh Kosong");
            return false;
        }else if(etProductDescription.getText().toString().trim().isEmpty()){
            etProductDescription.setError("Keterangan Produk Tidak Boleh Kosong");
            return false;
        }else if(selectedCategory.equals("- Pilih Kategori -")){
            Toast.makeText(getApplicationContext(),"Silahkan Pilih Kategori Produk", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}