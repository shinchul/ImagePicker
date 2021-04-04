package com.example.takepicture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONCODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Uri imagefile;
    Button mcapture_Image;
    ImageView mimage_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcapture_Image = findViewById(R.id.capture_Image);
        mimage_view = findViewById(R.id.image_view);

        mcapture_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmellow, request runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not enabled, request IT
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSIONCODE);
                    }else {
                        //permission already granted
                        openCamera();
                    }
                }else {
                    //system os < mashmallow
                    openCamera();
                }
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        imagefile = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_CAPTURE_CODE);
    }


    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user press allow or deny from permission requset popup
        switch (requestCode) {
            case PERMISSIONCODE: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    openCamera();
                }else {
                    //permission from popup was denied
                    Toast.makeText(this,"카메라권한좀", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    protected void onActivityResult(int requestCode, Intent data) {
        //called when image was captured from camera
        if (requestCode == RESULT_OK) {
            //set the image captured to our ImageView
            //mcapture_Image.setImageURI(imagefile);
        }
    }
}