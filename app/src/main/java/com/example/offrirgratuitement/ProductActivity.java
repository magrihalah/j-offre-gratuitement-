package com.example.offrirgratuitement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button btnbrowse, btnupload;
    EditText txtdata , numdata;
    ImageView imgview;
    Spinner spinner ;
    String item ;
    String[] villes = {"choisir une ville","Casablanca","Rabat","Marrakesh","Fes","Meknes","Tanger","Tetouan","Laayoun","Agadir","Safi"};

    TextInputEditText textInputEditText ;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        btnbrowse = (Button) findViewById(R.id.product_browse_btn);
        btnupload = (Button) findViewById(R.id.product_upload_btn);
        txtdata = (EditText) findViewById(R.id.product_input_name);
        spinner = (Spinner) findViewById(R.id.product_spinner);
        numdata = (EditText) findViewById(R.id.product_input_phone);
        textInputEditText = (TextInputEditText)findViewById(R.id.product_input_desc);
        imgview = (ImageView) findViewById(R.id.product_logo);
        progressDialog = new ProgressDialog(ProductActivity.this);// context name as per your project name



        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,villes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });


        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadImage();

            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void UploadImage() {

        if (FilePathUri != null) {
            if (item == "choose city" ){ Toast.makeText(getApplicationContext(), "please selcet a city ", Toast.LENGTH_SHORT).show();}
            else if(numdata.length()< 10 || numdata.length()> 10) {
                Toast.makeText(getApplicationContext(), "num incorect", Toast.LENGTH_SHORT).show();
            }
            else  {
                String TempImageName = txtdata.getText().toString().trim();
                String TempImageBio = textInputEditText.getText().toString().trim();
                String TempImageNum = numdata.getText().toString().trim();
                progressDialog.setTitle("Image is Uploading...");
                progressDialog.show();
                StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                storageReference2.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                @SuppressWarnings("VisibleForTests")
                                UploadInfo imageUploadInfo = new UploadInfo(TempImageName, TempImageBio, TempImageNum, item, taskSnapshot.getUploadSessionUri().toString());
                                String ImageUploadId = databaseReference.push().getKey();
                                databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            }
                        });
                Intent intent = new Intent(this,FeedActivity.class);
                startActivity(intent);
            }


        } else {

            Toast.makeText(ProductActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

}